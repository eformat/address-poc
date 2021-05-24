# Image URL to use all building/pushing image targets
REGISTRY ?= quay.io
REPOSITORY ?= $(REGISTRY)/eformat/address-poc

IMG := $(REPOSITORY):latest

# clean compile
compile:
	mvn clean package -DskipTests # -Dquarkus.package.type=legacy-jar #  -Pnative -Dquarkus.native.container-build=true -Dquarkus.native.container-runtime=podman -Dquarkus.profile=prod -Dquarkus.native.builder-image=quay.io/quarkus/ubi-quarkus-mandrel:20.3-java11

# Podman Login
podman-login:
	@podman login -u $(DOCKER_USER) -p $(DOCKER_PASSWORD) $(REGISTRY)

# Build the oci image no compile
podman-build-nocompile:
	podman build --no-cache . -t ${IMG} -f src/main/docker/Dockerfile.jvm

# Build the oci image
podman-build: compile
	podman build . -t ${IMG} -f src/main/docker/Dockerfile.jvm

# Push the oci image
podman-push: podman-build
	podman push ${IMG}

# Push the oci image
podman-push-nocompile: podman-build-nocompile
	podman push ${IMG}

# Just Push the oci image
podman-push-nobuild:
	podman push ${IMG}

podman-run:
	podman-compose -f docker-compose.yml up -d

podman-stop:
	podman-compose -f docker-compose.yml down
