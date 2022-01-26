pipeline {
  agent any
  stages {
    stage('') {
      steps {
        git(url: 'https://github.com/zhimin711/zh-admin.git', branch: 'master')
        sh '''mvn clean package -P npm -Denv=release -Dmaven.test.skip
'''
      }
    }

  }
}