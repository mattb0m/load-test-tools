FROM alpine:latest
RUN apk add openjdk11-jre-headless
WORKDIR /
COPY jmeter .
COPY run-test /jmeter/bin
ENV PATH $PATH:/jmeter/bin
ENTRYPOINT ["run-test"]
