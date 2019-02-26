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
        /* Ideally, we would run a test framework against our image.
         * For this example, we're using a Volkswagen-type approach ;-) */
    }

    stage('Push image') {
        /* Finally, we'll push the image with two tags:
         * First, the incremental build number from Jenkins
         * Second, the 'latest' tag.
         * Pushing multiple tags is cheap, as all the layers are reused. */
        docker.withRegistry('https://registry.hub.docker.com', 'docker-hub-credentials') {
            app.push("BUILD_NUMBER")
        }
    }

    stage('Remove Unused docker image') {
      steps{
        sh "docker rmi $registry:$BUILD_NUMBER"
      }
    }
}