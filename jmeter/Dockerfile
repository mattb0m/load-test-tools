FROM alpine:latest
RUN apk add openjdk11-jre-headless
COPY jmeter /jmeter
COPY run-test /jmeter/bin
ENV PATH="/jmeter/bin:${PATH}"
ENTRYPOINT ["run-test"]
