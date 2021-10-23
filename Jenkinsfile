pipeline {
	agent any
	stages {
		stage('Build Backend') {
			steps {
				bat 'mvn clean package -DskipTests=true'
			}
		}
		stage('Unit Tests') {
			steps {
				bat 'mvn test'
			}
		}
		stage('Sonar Analysis') {
			environment {
				scannerHome = tool 'SONAR_SCANNER'
			}
			steps {
				withSonarQubeEnv('SONAR_LOCAL') {
					bat "${scannerHome}/bin/sonar-scanner -e -Dsonar.projectKey=DeployBack -Dsonar.host.url=http://localhost:9000 -Dsonar.login=910097c2abee907a569469ed3e4ab97669b55790 -Dsonar.java.binaries=target -Dsonar.coverage.exclusions=**/.mvn/**,**/src/test/**,**/model/**,**Application.java"
				}
			}
		}
		stage('Quality Gate'){
			steps {
				sleep(5)
				timeout(time: 1, unit: 'MINUTES'){
					waitForQualityGate abortPipeline: true
				}
			}
		}
		stage('Deploy Backend'){
			steps{
				deploy adapters: [tomcat8(credentialsId: 'TomcatLogin', path: '', url: 'http://localhost:8001/')], contextPath: 'tasks-backend', war: 'target/tasks-backend.war'
			}
		}
		stage('API Tests'){
			steps{
				dir('api-test'){
					git 'https://github.com/andrebueno/tasks-api-test'
					bat 'mvn test'
				}
			}
		}
		stage('Build Frontend') {
			steps{
				dir('frontend'){
					git 'https://github.com/andrebueno/tasks-frontend'
					bat 'mvn clean package'
				}
			}
		}
		stage('Deploy Frontend'){
			steps{
				dir('frontend'){
					deploy adapters: [tomcat8(credentialsId: 'TomcatLogin', path: '', url: 'http://localhost:8001/')], contextPath: 'tasks', war: 'target/tasks.war'
				}
			}
		}
		stage('Functional Tests'){
			steps{
				dir('functional-test'){
					git 'https://github.com/andrebueno/tasks-functional-tests'
					bat 'mvn test'
				}
			}
		}
		stage('Deploy Prod'){
			steps{
				bat 'docker-compose build'
				bat 'docker-compose up -d'
			}
		}
		stage('Health Check'){
			steps{
				sleep(5)
				dir('functional-test'){
					bat 'mvn verify -Dskip.surefire.tests'
				}
			}
		}
	}
	post{
		always{
			junit allowEmptyResults: true, testResults: 'target/surefire-reports/*.xml, api-test/target/surefire-reports/*.xml, functional-test/target/surefire-reports/*.xml, functional-test/target/failsafe-reports/*.xml'
		}
		//unsuccessful {
		//	emailext attachLog: true, body: 'See the attached log below', subject 'Build $BUILD_NUMBER has failed', to: 'decobueno+jenkins@gmail.com'
		//}
		//fixed {
		//	emailext attachLog: true, body: 'See the attached log below', subject 'Build is fine!!!', to: 'decobueno+jenkins@gmail.com'
		//}
	}
}
