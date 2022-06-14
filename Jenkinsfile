#!/usr/bin/env groovy

def prKey = "-Dsonar.pullrequest.key=${env.CHANGE_ID}"
def prBranch = "-Dsonar.pullrequest.branch=${env.CHANGE_BRANCH}"
def prBase = "-Dsonar.pullrequest.base=${env.CHANGE_TARGET}"

pipeline {
  agent {
    label 'java/11-docker-ecr && environment/qa && account/bv-nexus-qa'
  }

  options {
    timeout(time: 20, unit: 'MINUTES')
    buildDiscarder(logRotator(numToKeepStr: '20'))
    disableConcurrentBuilds()
    timestamps()
  }

  tools {
    maven 'maven-3.6.3'
  }

  stages {
     stage('Build') {
      steps {
        withSonarQubeEnv(installationName: 'Bazaarvoice QA SonarQube') {
          sh '''mvn -e verify -Ddependency-check.skip=true sonar:sonar ${prKey} ${prBranch} ${prBase}'''
        }
      }
    }
  }

  post {
    success {
      script {
        setBuildStatus("Build succeeded", "SUCCESS")
      }
    }
    unsuccessful {
      script {
        setBuildStatus("Build failed", "FAILED")
      }
    }
  }
}

def void setBuildStatus(String message, String state) {
  step([
    $class            : "GitHubCommitStatusSetter",
    reposSource       : [$class: "ManuallyEnteredRepositorySource", url: 'git@github.com:bazaarvoice/rison.git'],
    contextSource     : [$class: "ManuallyEnteredCommitContextSource", context: "ci/jenkins/build-status"],
    errorHandlers     : [[$class: "ChangingBuildStatusErrorHandler", result: "UNSTABLE"]],
    statusResultSource: [$class: "ConditionalStatusResultSource", results: [[$class: "AnyBuildResult", message: message, state: state]]]
  ])
}
