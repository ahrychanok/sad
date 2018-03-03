<?xml version="1.0" encoding="UTF-8"?>
<Configuration status="WARN" shutdownHook="disable">
  <Appenders>
    <Console name="Console" target="SYSTEM_OUT">
      <PatternLayout pattern="%d{HH:mm:ss.SSS} [%t] %-5level %logger{36} - %msg%n"/>
    </Console>
    <Console name="Audit" target="SYSTEM_OUT">
      <PatternLayout pattern="%d{yyyyMMdd'T'HH:mm:ss.SSS} %msg%n" />
    </Console>
  </Appenders>
  <Loggers>
    <Logger name="admin-api" level="INFO" additivity="false">
      <AppenderRef ref="Console"/>
    </Logger>
    <Logger name="system-audit" level="INFO" additivity="false">
      <AppenderRef ref="Audit"/>
    </Logger>

    <Root level="INFO">
      <AppenderRef ref="Console"/>
    </Root>
  </Loggers>
</Configuration>
