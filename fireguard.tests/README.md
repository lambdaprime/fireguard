It is easier to run tests inside `fireguard` docker container. See [Documentation](http://portal2.atwebpages.com/fireguard) how to build docker image for it.

To run tests:

```
cd FIREGUARD_PROJECT
docker run --privileged --device=/dev/kvm \
 -v ~/.m2:/home/ubuntu/.m2 \
 -v GRADLE_FOLDER:/home/ubuntu/opt/gradle \
 -v `pwd`:/home/ubuntu/fireguard-dev \
 -it fireguard
cd fireguard-dev/
```
