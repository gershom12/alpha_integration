#
# Team Alpha Glassfish Dockerfile
#

# Pull the base fs layer
FROM ubuntu:latest

# Maintainer
# ----------
MAINTAINER Andrew Broekman <u11089777@tuks.co.za>

# Install and configure Oracle JDK 8 Environment
# ----------------------------------------------
RUN echo 'deb http://ppa.launchpad.net/webupd8team/java/ubuntu trusty main' > /etc/apt/sources.list.d/webupd8team-java.list && \
    echo 'deb-src http://ppa.launchpad.net/webupd8team/java/ubuntu trusty main' >> /etc/apt/sources.list.d/webupd8team-java.list && \
    apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv-keys EEA14886 && \
    apt-get update && \
    echo debconf shared/accepted-oracle-license-v1-1 select true | debconf-set-selections  && \
    echo debconf shared/accepted-oracle-license-v1-1 seen true | debconf-set-selections  && \
    DEBIAN_FRONTEND=noninteractive apt-get install -y oracle-java8-installer oracle-java8-set-default curl unzip && \
    apt-get purge -y --auto-remove && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/* /tmp/* /var/tmp/*
ENV JAVA_HOME /usr/lib/jvm/java-8-oracle

# Environment variables required for this build
# ---------------------------------------------
ENV GLASSFISH_PACKAGE http://download.java.net/glassfish/4.1.1/release/glassfish-4.1.1.zip
ENV GLASSFISH_PACKAGE_FILENAME glassfish-4.1.1.zip
ENV CONFIG_JVM_ARGS -Djava.security.egd=file:/dev/./urandom

# Setup and configure Glassfish 4 Environment
# -------------------------------------------
ADD integration-1.0-SNAPSHOT.war /tmp
ADD glassfish-4.1.zip /tmp
ADD postgresql-9.4.1208.jar /tmp
RUN useradd -b /opt -m -s /bin/bash glassfish && \
    echo glassfish:glassfish | chpasswd && \
    cd /opt/glassfish && \
    mv /tmp/glassfish-4.1.zip /opt/glassfish && \
    unzip glassfish-4.1.zip && \
    chown -R glassfish:glassfish /opt/glassfish* && \
    cd /opt/glassfish/glassfish4/bin && \
    mv /tmp/postgresql-9.4.1208.jar /opt/glassfish/glassfish4/glassfish/domains/domain1/lib && \


    # User: admin / Pass: glassfish
    # -----------------------------
    echo "admin;{SSHA256}80e0NeB6XBWXsIPa7pT54D9JZ5DR5hGQV1kN1OAsgJePNXY6Pl0EIw==;asadmin" > /opt/glassfish/glassfish4/glassfish/domains/domain1/config/admin-keyfile && \
    echo "AS_ADMIN_PASSWORD=glassfish" > pwdfile && \

    # Setup security and deploy application
    ./asadmin start-domain && \
    ./asadmin --interactive=false --user admin --passwordfile pwdfile deploy /tmp/integration-1.0-SNAPSHOT.war && \
    ./asadmin --interactive=false --user admin --passwordfile pwdfile enable-secure-admin && \
    ./asadmin stop-domain && \
    echo "export PATH=$PATH:/opt/glassfish/glassfish4/bin" >> /opt/glassfish/.bashrc && \
    chown -R glassfish:glassfish /opt/glassfish* && \

    # Clean up
    rm /tmp/integration-1.0-SNAPSHOT.war

# Add key and CA store with required SSL certificates
# ---------------------------------------------------
ADD keystore.jks /opt/glassfish/glassfish4/glassfish/domains/domain1/config
ADD cacerts.jks /opt/glassfish/glassfish4/glassfish/domains/domain1/config

# Set Glassfish user in its /home/bin by default
# ----------------------------------------------
USER glassfish
WORKDIR /opt/glassfish/glassfish4/bin

# Expose default Glassfish ports
# ------------------------------
EXPOSE 4848 8009 8080 8181

CMD ["/opt/glassfish/glassfish4/bin/asadmin", "start-domain", "--verbose=true"]



