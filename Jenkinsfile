pipeline {
    agent any

    // ──────────────────────────────────────────────
    // Global tool references (configure in Jenkins →
    // Manage Jenkins → Tools with these exact names)
    // ──────────────────────────────────────────────
    tools {
        jdk   'JDK-21'
        maven 'Maven-3.9'
    }

    // ──────────────────────────────────────────────
    // Build parameters – exposed on the "Build with
    // Parameters" UI; defaults work for every run.
    // ──────────────────────────────────────────────
    parameters {
        choice(
            name: 'ENV',
            choices: ['dev', 'qa'],
            description: 'Target environment to run tests against'
        )
        choice(
            name: 'BROWSER',
            choices: ['chromium', 'firefox', 'webkit'],
            description: 'Browser to use for UI tests'
        )
        string(
            name: 'GROUPS',
            defaultValue: 'uitest,apitest',
            description: 'Comma-separated TestNG groups to run'
        )
        booleanParam(
            name: 'HEADLESS',
            defaultValue: true,
            description: 'Run browser in headless mode (must be true on a headless agent)'
        )
    }

    // ──────────────────────────────────────────────
    // Pipeline-level environment variables
    // ──────────────────────────────────────────────
    environment {
        ALLURE_RESULTS_DIR = 'target/allure-results'
    }

    // ──────────────────────────────────────────────
    // Pipeline stages
    // ──────────────────────────────────────────────
    stages {

        stage('Checkout') {
            steps {
                echo "Checking out source code..."
                git(
                    url          : 'git@github.com:officialdeepurajagopal/PlayWrightJavaHybridFramework.git',
                    branch       : 'main',
                    credentialsId: 'github-ssh-key'
                )
            }
        }

        stage('Build') {
            steps {
                echo "Compiling project..."
                sh 'mvn clean compile -q'
            }
        }

        stage('Install Playwright Browsers') {
            steps {
                echo "Installing Playwright browser binaries..."
                // Install the browser binaries (uses the default OS cache location)
                sh """
                    mvn exec:java \
                        -e \
                        -Dexec.mainClass=com.microsoft.playwright.CLI \
                        -Dexec.args="install ${params.BROWSER}"
                """
            }
        }

        stage('Install Playwright System Dependencies') {
            steps {
                echo "Installing OS-level system dependencies required by Playwright browsers..."
                // 'install-deps' uses apt-get and is Linux-only.
                // macOS already ships with the required system libraries, so we skip it there.
                sh """
                    if [ "\$(uname)" = "Linux" ]; then
                        echo "Linux detected — running install-deps to install system libraries..."
                        mvn exec:java \
                            -e \
                            -Dexec.mainClass=com.microsoft.playwright.CLI \
                            -Dexec.args="install-deps ${params.BROWSER}"
                    else
                        echo "macOS detected — skipping install-deps (system libraries are pre-installed by the OS)."
                    fi
                """
            }
        }

        stage('Test') {
            steps {
                echo "Running tests on env=${params.ENV}, browser=${params.BROWSER}, headless=${params.HEADLESS}, groups=${params.GROUPS}..."
                sh """
                    mvn test \
                        -Denv=${params.ENV} \
                        -Dbrowser=${params.BROWSER} \
                        -Dheadless=${params.HEADLESS} \
                        -Dslowmo=0 \
                        -Dgroups="${params.GROUPS}"
                """
            }
        }
    }

    // ──────────────────────────────────────────────
    // Post-build actions
    // ──────────────────────────────────────────────
    post {
        always {
            echo "Archiving test artifacts..."

            // Archive raw Allure results and Surefire XML reports
            archiveArtifacts artifacts: 'target/allure-results/**', allowEmptyArchive: true
            archiveArtifacts artifacts: 'target/surefire-reports/**', allowEmptyArchive: true

            // Generate and publish the Allure HTML report
            // Requires the "Allure Jenkins Plugin" to be installed
            allure([
                includeProperties: false,
                jdk              : '',
                results          : [[path: "${ALLURE_RESULTS_DIR}"]]
            ])
        }

        success {
            echo "✅ Pipeline completed successfully."
        }

        failure {
            echo "❌ Pipeline failed. Check test results and logs above."
            // Uncomment the block below after configuring the Email Notification
            // plugin (Manage Jenkins → Configure System → Extended E-mail Notification):
            //
            // emailext(
            //     subject : "FAILED: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
            //     body    : "Build URL: ${env.BUILD_URL}",
            //     to      : "your-team@example.com"
            // )
        }

        unstable {
            echo "⚠️ Pipeline is unstable (some tests may have failed)."
        }
    }
}
