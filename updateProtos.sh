#!/bin/bash

EXTERNAL_REPO="git@github.com:input-output-hk/psg-services.git"
EXTERNAL_REPO_BRANCH="develop"
UNIQUE_TAG="metadata-service"
EXTERNAL_REPO_PATH="cardano-metadata-service/src/main/protobuf"
TARGET_BRANCH=${EXTERNAL_REPO_BRANCH}
TARGET_PATH="protos/cardano-metadata-service/protobuf"


./mergeToSubfolderFromRemoteSubfolder.sh ${EXTERNAL_REPO} ${EXTERNAL_REPO_BRANCH} ${UNIQUE_TAG} ${EXTERNAL_REPO_PATH} ${TARGET_BRANCH} ${TARGET_PATH}

EXTERNAL_REPO_PATH="store-and-hash-service/src/main/protobuf"
UNIQUE_TAG="storeandhash-service"
TARGET_PATH="protos/store-and-hash-service/protobuf"

./mergeToSubfolderFromRemoteSubfolder.sh ${EXTERNAL_REPO} ${EXTERNAL_REPO_BRANCH} ${UNIQUE_TAG} ${EXTERNAL_REPO_PATH} ${TARGET_BRANCH} ${TARGET_PATH}


EXTERNAL_REPO_PATH="common-service/src/main/protobuf"
UNIQUE_TAG="common-service"
TARGET_PATH="protos/common-service/protobuf"

./mergeToSubfolderFromRemoteSubfolder.sh ${EXTERNAL_REPO} ${EXTERNAL_REPO_BRANCH} ${UNIQUE_TAG} ${EXTERNAL_REPO_PATH} ${TARGET_BRANCH} ${TARGET_PATH}
