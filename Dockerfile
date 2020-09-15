FROM adoptopenjdk/openjdk13:alpine as BUILDER

LABEL maintainer="Adrien Navratil <adrien1975@live.fr>"

ENV BUILD_ROOT /tmp/shenron-build

# Setting up build project files
RUN mkdir -p $BUILD_ROOT
WORKDIR $BUILD_ROOT

COPY gradle ./gradle
COPY src ./src
COPY build.gradle gradlew settings.gradle ./

# Building Shenron
RUN ./gradlew distTar
RUN tar xf $BUILD_ROOT/bot/build/distributions/shenron-*.tar
RUN mkdir -p /tmp/shenron-final
RUN mv shenron-*/* /tmp/shenron-final


# Reseting the image build with a clean Alpine Linux
FROM adoptopenjdk/openjdk11:alpine-jre

ENV USER shenron
ENV SHENRON_ROOT /var/run/shenron

# Creating the runner user
RUN addgroup -g 1000 $USER && adduser -u 1000 -D -G $USER $USER

# Setting up runtime project files
RUN mkdir -p $SHENRON_ROOT
WORKDIR $SHENRON_ROOT

RUN chown $USER:$USER $SHENRON_ROOT

USER $USER

# Copying files from BUILDER step
COPY --from=BUILDER /tmp/shenron-final ./

# Copying run script
COPY docker_run.sh ./run

# Final settings
CMD ["/bin/sh", "./run"]