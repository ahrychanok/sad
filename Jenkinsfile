node('master') {
    
    stage('Checkout') {
        checkout scm
    }
    
    stage('Build') {
        maven('clean package -DskipTests')
    }
    
}