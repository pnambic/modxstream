#! /bin/bash
# Trigger a release via GitHub workflow on 'release/' tags
set -o nounset
set -o errexit

readonly DIRNAME=$(dirname "$0")
readonly SELF=$(cd "${DIRNAME}" && pwd)
readonly RELPATH='releases'

function releaseAndPush {
  local tagName="$1"
  local tagMsg="$2"

  git tag -m "${tagMsg}" "${tagName}"
  git push origin tag "${tagName}"
}

function usage {
  cat >&2 <<HELP
Usage: $(basename "$0") tagLabel tagMsg
  Create a release tag and push it to GitHub to trigger a release
  Both the tag label and tagMsg are expected
  The prefix \'release/\' is automatically prepended to the tagLabel
HELP
}

function die {
  echo "$1" >&2
  usage
  exit 1
}

function main {

  if [ $# -eq 1 ]; then
    releaseAndPush "${RELPATH}/${1}" "Release ${1}"
    exit 0
  fi

  if [ $# -ne 2 ]; then
    releaseAndPush "${RELPATH}/${1}" "${2}"
    exit 0
  fi

  die "Release requires a label and a message"
}

main "$@"
