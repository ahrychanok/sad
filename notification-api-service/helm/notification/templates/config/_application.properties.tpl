# Application name
application.name=NotificationService
# server port
server.port=8080
# management port
management.port=8101
# Broker and queue service configuration
notification.service.internal.queue.queueName=com.smarsh.notificationservice.messages
notification.service.internal.queue.errorQueueName=com.smarsh.notificationservice.messages.error
notification.service.internal.queue.connectionString=tcp://{{ .Values.ActiveMQ.Host }}:{{ .Values.ActiveMQ.Port }}
notification.service.internal.queue.defaultTimeout=100
notification.service.internal.queue.retryTimeout=100
notification.service.internal.queue.retryCount=-1
notification.service.internal.queue.queueConsumersCount=1
# Broker and queue service configuration
notification.service.restcallback.queue.queueName=com.smarsh.notificationservice.restcallback
notification.service.restcallback.queue.errorQueueName=com.smarsh.notificationservice.restcallback.error
notification.service.restcallback.queue.connectionString=tcp://{{ .Values.ActiveMQ.Host }}:{{ .Values.ActiveMQ.Port }}
notification.service.restcallback.queue.defaultTimeout=100
notification.service.restcallback.queue.retryTimeout=100
notification.service.restcallback.queue.retryCount=-1
notification.service.restcallback.queue.queueConsumersCount=1
#Storage configuration
notification.service.storage.service.maximumRetryCount=5
notification.service.storage.service.retryDelayMs=500
#S3 template storage location and credentials
notification.service.templates.s3.uri=
notification.service.templates.s3.bucket=
notification.service.templates.s3.value=
notification.service.templates.s3.secret=
# SMTP settings
notification.service.mail.server.host=email-smtp.us-west-2.amazonaws.com
notification.service.mail.server.port=587
notification.service.mail.server.protocol=smtp
notification.service.mail.server.username=AKIAJXOXN3XXINCZGTOQ
notification.service.mail.server.password=AnigU94gSxFYekSTAg31OdyWuHV+tG2tF5M5cXzlZM+s
notification.service.mail.server.auth=true
notification.service.mail.server.tlsEnabled=false
notification.service.mail.server.quitWait=false
notification.service.mail.server.defaultSender=noreply@smarsh.com

#FILESYSTEM PATH FOR LOCAL SERVICE TESTING KEEP THIS PROPERTY EMPTY!!!!
notification.service.templates.filesystemPath=
#Templates cache ttl
notification.service.templates.cacheTTL=10
# Key service client configuration
keyservice.client.url=http://{{ .Values.KeyServiceAPI.Host }}:{{ .Values.KeyServiceAPI.Port }}
keyservice.client.cacheRefreshInterval=5
keyservice.client.cryptoperiod=5

# Archive API Connection String
archive.api.url=http://{{ .Values.ArchiveAPI.Host }}:{{ .Values.ArchiveAPI.Port }}
