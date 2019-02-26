node {
    def REGISTRY = "lincongca01/springboot-demo"
    def BUILD_NUMBER = "latest"
    def CONTAINER_NAME = "demo-app"
    def PORT = "8081"
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
        app.inside {
            try {
                sh ```
                    docker run -d --name $CONTAINER_NAME -p $PORT:$PORT $REGISTRY:$BUILD_NUMBER
                    docker_host_ip = `docker-machine.exe ip win-docker-host`
                    res = `curl http://${docker_host_ip}:$PORT/api/hello`
                    echo "${res: -1}"
                    if ["1" == "${res: -1}"]; then
                        'exit 0'
                    else
                        'exit 1'
                    fi
                ```
            }
            catch (exc) {
                echo 'Something failed, I should sound the klaxons!'
                throw
            }
        }
        post {
            always {
                sh ```
                    docker stop $CONTAINER_NAME
                    docker rm $CONTAINER_NAME
                ```
            }
            failure {
                sh "docker rmi $REGISTRY:$BUILD_NUMBER"
            }
        }
    }

    stage('Push image') {
        /* Finally, we'll push the image with two tags:
         * First, the incremental build number from Jenkins
         * Second, the 'latest' tag.
         * Pushing multiple tags is cheap, as all the layers are reused. */
        docker.withRegistry('', 'docker-hub-credentials') {
            app.push(BUILD_NUMBER)
        }
    }

    stage('Remove Unused docker image') {
        sh "docker rmi $REGISTRY:$BUILD_NUMBER"
    }
}