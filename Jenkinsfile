pipeline {
    agent any

    // ──────────────────────────────────────────────
    // Global tool references (configure in Jenkins →
    // Manage Jenkins → Tools with these exact names)
    // ──────────────────────────────────────────────
    tools {
        jdk   'JDK-17'
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
        ALLURE_RESULTS_DIR = 'allure-results'
        // Inject credentials stored in Jenkins Credential Manager.
        // Create two "Secret text" credentials with IDs below and
        // they will be available as env vars inside the Test stage.
        APP_USERNAME = credentials('APP_USERNAME')
        APP_PASSWORD = credentials('APP_PASSWORD')
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
                echo "Installing Playwright browser binaries and OS dependencies..."
                // Downloads the browser selected as a parameter (plus its system deps).
                // On a fresh Linux agent this installs libwoff, gstreamer, etc. automatically.
                sh """
                    mvn exec:java \
                        -e \
                        -Dexec.mainClass=com.microsoft.playwright.CLI \
                        -Dexec.args="install --with-deps ${params.BROWSER}"
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
                        -Dgroups="${params.GROUPS}" \
                        -Dusername=${APP_USERNAME} \
                        -Dpassword=${APP_PASSWORD}
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
            archiveArtifacts artifacts: 'allure-results/**', allowEmptyArchive: true
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

