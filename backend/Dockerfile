FROM ubuntu:16.04
label maintainer=<malyjasiak.jakub@gmail.com>

# Basics
RUN apt update && apt upgrade -y
RUN apt install -y curl unzip wget zip build-essential checkinstall cmake pkg-config yasm git gfortran software-properties-common

# java
RUN add-apt-repository -y ppa:webupd8team/java \
	&& apt update \
	&& echo oracle-java8-installer shared/accepted-oracle-license-v1-1 select true | /usr/bin/debconf-set-selections \
	&& apt install -y oracle-java8-installer oracle-java8-set-default

ENV JAVA_HOME /usr/lib/jvm/java-8-oracle

RUN apt install -y ant

ENV ANT_HOME /usr/bin/ant

# Bitmap utils
RUN apt-get install -y libjpeg8-dev libjasper-dev libpng12-dev libtiff5-dev

RUN apt-get install -y libavcodec-dev libavformat-dev libswscale-dev libdc1394-22-dev
RUN apt-get install -y libxine2-dev libv4l-dev
RUN apt-get install -y libgstreamer0.10-dev libgstreamer-plugins-base0.10-dev
RUN apt-get install -y qt5-default libgtk2.0-dev libtbb-dev
RUN apt-get install -y libatlas-base-dev
RUN apt-get install -y libfaac-dev libmp3lame-dev libtheora-dev
RUN apt-get install -y libvorbis-dev libxvidcore-dev
RUN apt-get install -y libopencore-amrnb-dev libopencore-amrwb-dev
RUN apt-get install -y x264 v4l-utils
 
# Optional dependencies
RUN apt-get install -y libprotobuf-dev protobuf-compiler
RUN apt-get install -y libgoogle-glog-dev libgflags-dev
RUN apt-get install -y libgphoto2-dev libeigen3-dev libhdf5-dev doxygen

# Python
RUN apt-get install -y python-dev python-pip python3-dev python3-pip

RUN mkdir -p /theeye
WORKDIR /theeye
COPY . .

# Opencv
RUN wget https://github.com/opencv/opencv/archive/3.3.1.zip
RUN mkdir opencv \
	&& mv 3.3.1.zip opencv \
	&& cd opencv \
	&& unzip 3.3.1.zip \
	&& cd opencv-3.3.1 \
	&& mkdir build \
	&& cd build \
	&& cmake -DBUILD_SHARED_LIBS=OFF .. \
	&& make -j8 \
	&& ls && pwd && cd lib && ls

RUN ln /theeye/opencv/opencv-3.3.1/build/lib/libopencv_java331.so /lib/
EXPOSE 8080

# build app
RUN ./gradlew wrapper \
	&& ./gradlew clean compilejava jar \
	&& ./gradlew bootrepackage 

ENTRYPOINT java -jar build/libs/theeye.jar
