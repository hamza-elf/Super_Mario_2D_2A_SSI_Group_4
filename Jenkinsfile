
pipeline {
    agent any
    stages {
        stage('git repo & clean') {
            steps {
                //bat "rmdir  /s /q TestJenkins"
                bat "git clone https://github.com/hamza-elf/Super_Mario_2D_2A_SSI_Group_4.git"
                bat "mvn clean -f TestJenkins"
            }
        }
        stage('install') {
            steps {
                bat "mvn install -f TestJenkins"
            }
        }
        stage('test') {
            steps {
                bat "mvn test -f TestJenkins"
            }
        }
        stage('package') {
            steps {
                bat "mvn package -f TestJenkins"
            }
        }
    }
}
