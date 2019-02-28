node {
    def REGISTRY = "lincongca01/springboot-demo"
    def BUILD_NUMBER = "latest"
    def app

    stage('Clone repository') {
        /* Let's make sure we have the repository cloned to our workspace */
        checkout scm
    }

    stage('Build image') {
        withMaven(
          maven: 'M3',
          jdk: 'jdk-latest') {
            sh "mvn clean install"
        }
        
        /* This builds the actual image; synonymous to
        * docker build on the command line */
        app = docker.build(REGISTRY)
    }
    stage('Test image') {
        app.withRun('-p 8081:8080 --name demo-app') {
            def res = sh(script: 'while ! curl http://192.168.99.100:8081/api/hello; do sleep 1; done', returnStdout: true)
            sh "echo $res"
        }
    }

    stage('Push image') {
        /* Finally, we'll push the image with two tags:
         * First, the incremental build number from Jenkins
         * Second, the 'latest' tag. */
        docker.withRegistry('', 'docker-hub-credentials') {
            app.push("${env.BUILD_NUMBER}")
            app.push(BUILD_NUMBER)
        }
    }

    stage('Remove Unused docker image') {
        sh "docker rmi $REGISTRY:$BUILD_NUMBER"
    }
}