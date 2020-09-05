#!/bin/bash
# Ref: https://gist.github.com/tswaters/542ba147a07904b1f3f5
# https://jrsmith3.github.io/merging-a-subdirectory-from-another-repo-via-git-subtree.html

if [ "$#" -ne 6 ]; then
    echo "Illegal number of parameters"
    echo "<external repo> <external repo branch> <unique tag> <path in external repo> <target branch> <non preexisting target path in this repo>"
    exit
fi

EXTERNAL_REPO=$1 # e.g. git@yada.yada.git
EXTERNAL_REPO_BRANCH=$2 # e.g. master
UNIQUE_TAG=$3 # e.g. metadata-service
EXTERNAL_REPO_PATH=$4 # e.g. cardano-metadata-service/src/main/protobuf
TARGET_BRANCH=$5 # e.g. master
TARGET_PATH=$6 # e/g/ protos/cardano-metadata-service/protobuf

NEW_REMOTE=${UNIQUE_TAG}-upstream
TRACKING_BRANCH=${NEW_REMOTE}/tracking-${UNIQUE_TAG}

git checkout ${TARGET_BRANCH}

isOkToContinue=$("./currentBranchStatus.sh")

if [ "${isOkToContinue}" == "Up-to-date" ]; then
    # add remote, create new tracking branch 
    git remote add -f ${NEW_REMOTE} ${EXTERNAL_REPO}

    git checkout -b ${TRACKING_BRANCH} ${NEW_REMOTE}/${EXTERNAL_REPO_BRANCH}


    # split off subdir of tracking branch into separate branch
    git subtree split -q --squash --prefix=${EXTERNAL_REPO_PATH} --annotate='[${UNIQUE_TAG}] ' --rejoin -b merging/${UNIQUE_TAG}

    # add separate branch as subdirectory.
    git checkout ${TARGET_BRANCH}

    git subtree add --prefix=${TARGET_PATH} --squash merging/${UNIQUE_TAG}

    git branch -D ${TRACKING_BRANCH} merging/${UNIQUE_TAG}
    git remote rm ${NEW_REMOTE}

else 
    echo "Branch not up to date"
    exit
fi
