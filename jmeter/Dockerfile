FROM openjdk:18-jdk-alpine
RUN apk add --update --no-cache python3 curl perl && ln -sf python3 /usr/bin/python
WORKDIR /jmeter
COPY get_jmeter .
COPY run_test_jm .
RUN ["/bin/sh", "get_jmeter"]
RUN ln -sf "/jmeter/$(find apache-jmeter* -maxdepth 0 -type d)/bin/jmeter" /usr/bin/jmeter
RUN ln -sf "/jmeter/run_test_jm" /usr/bin/run_test_jm
CMD ["jmeter", "-v"]
