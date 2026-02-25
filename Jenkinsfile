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
        ALLURE_RESULTS_DIR       = 'allure-results'
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

        stage('Install Playwright OS Dependencies') {
            steps {
                echo "Installing Playwright system-level OS dependencies via apt-get..."
                // Playwright's built-in 'install-deps' internally calls 'su' which fails in
                // Docker containers where the jenkins user is not root.
                // Instead, we install the required native libraries directly with apt-get.
                // The Jenkins Docker image already runs as root, so no sudo is needed.
                sh """
                    apt-get update -qq && apt-get install -y --no-install-recommends \
                        libnss3 \
                        libnspr4 \
                        libatk1.0-0 \
                        libatk-bridge2.0-0 \
                        libcups2 \
                        libdrm2 \
                        libdbus-1-3 \
                        libxkbcommon0 \
                        libxcomposite1 \
                        libxdamage1 \
                        libxfixes3 \
                        libxrandr2 \
                        libgbm1 \
                        libasound2 \
                        libpango-1.0-0 \
                        libcairo2 \
                        libxshmfence1 \
                        fonts-liberation \
                        libvulkan1 \
                        xdg-utils
                """
            }
        }

        stage('Install Playwright Browsers') {
            steps {
                echo "Verifying Playwright browser binaries..."
                // Use $HOME from the Jenkins shell environment to locate the browser cache.
                // This avoids hardcoded paths and permission errors.
                sh """
                    export PLAYWRIGHT_BROWSERS_PATH="\$HOME/ms-playwright"
                    echo "PLAYWRIGHT_BROWSERS_PATH=\$PLAYWRIGHT_BROWSERS_PATH"
                    mvn exec:java \
                        -e \
                        -Dexec.mainClass=com.microsoft.playwright.CLI \
                        -Dexec.args="install ${params.BROWSER}"
                """
            }
        }

        stage('Test') {
            steps {
                echo "Running tests on env=${params.ENV}, browser=${params.BROWSER}, headless=${params.HEADLESS}, groups=${params.GROUPS}..."
                sh """
                    export PLAYWRIGHT_BROWSERS_PATH="\$HOME/ms-playwright"
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
            node(null) {
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

