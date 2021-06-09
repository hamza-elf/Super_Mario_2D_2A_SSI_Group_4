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
                bat 'ant tests-unitaires'
            }
        }
        stage('Packaging phase') {
            steps {
                bat 'ant packages'
            }
        }
    }
}
