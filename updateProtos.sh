#!/bin/bash

# NB!!! Remove the local protos folder first and commit the removal, make sure the local repo is 'clean'

EXTERNAL_REPO="git@github.com:input-output-hk/psg-services.git"
EXTERNAL_REPO_BRANCH="develop"
TARGET_BRANCH=${EXTERNAL_REPO_BRANCH}

function update_proto() {
  EXTERNAL_REPO_PATH=$1
  UNIQUE_TAG=$2
  TARGET_PATH=$3

  ./mergeToSubfolderFromRemoteSubfolder.sh ${EXTERNAL_REPO} ${EXTERNAL_REPO_BRANCH} ${UNIQUE_TAG} ${EXTERNAL_REPO_PATH} ${TARGET_BRANCH} ${TARGET_PATH}
}

update_proto "cardano-metadata-service/src/main/protobuf" "metadata-service" "protos/cardano-metadata-service/protobuf"
update_proto "store-and-hash-service/src/main/protobuf" "storeandhash-service" "protos/store-and-hash-service/protobuf"
update_proto "common-service/src/main/protobuf" "common-service" "protos/common-service/protobuf"
update_proto "native-assets-service/src/main/protobuf" "native-assets-service" "protos/native-assets-service/protobuf"