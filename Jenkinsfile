pipeline {
    agent any

    stages {
        stage('Building phase') {
            steps {
                withAnt(installation:'ant'){
                    bat 'ant compile'
                }
            }
        }
        stage('Testing phase') {
            steps {
                bat 'ant unit-tests'
            }
        }
        stage('Packaging phase') {
            steps {
                bat 'ant package'
            }
        }
    }
}
