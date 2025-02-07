#! /bin/bash
# Trigger a release via GitHub workflow on 'release/' tags
set -o nounset
set -o errexit

readonly DIRNAME=$(dirname "$0")
readonly SELF=$(cd "${DIRNAME}" && pwd)
readonly RELPATH='releases'

function check {
  local relVer="$1"

  (git branch | grep -qx '* base') || die 'Must be on base branch'
  [ -z "$(git status --porcelain)" ] || die 'Working tree must be clean'
  grep -q -e "<version>${relVer}-SNAPSHOT</version>" pom.xml || die 'Unexpected version in pom.xml'
}

function editVersion {
  local currVer="$1"
  local nextVer="$2"

  local currText="\\<version>${currVer}-SNAPSHOT\\</version>"
  # Caller includes -SNAPSHOT if needed
  local nextText="\\<version>${nextVer}\\</version>"

  echo sed -f pom.xml -e "\\%${currText}%s%${currText}%${nextText}%"
  sed -f pom.xml -e "\\%${currText}%s%${currText}%${nextText}%"
}

function showVersion {
  grep -e '<version>' pom.xml | head -n1
}

function gitPush {
  echo git push "$@"
  # git push "$@"
}

function releaseAndPush {
  local relVer="$1"
  local devVer="$2"
  local relBranch="releases/${relVer}"

  local tagName="${RELPATH}/${relVer}"
  local tagMsg="Release ${relVer}"

  # Confirm proper starting conditions
  check "${relVer}"

  # Mark the start
  git branch "${relBranch}"

  # Update base branch
  # EDIT TO NEXT RELEASE WITH -SNAPSHOT
  editVersion "${relVer}" "${devVer}-SNAPSHOT"
  git add -u
  git commit -m 'Update version for next release'
  gitPush

  # Prepare release branch.
  git checkout "${relBranch}"
  # EDIT REMOVE -SNAPSHOT
  editVersion "${devVer}" "${devVer}"
  git add -u
  git commit -m "Commit ${relVer} release"
  gitPush

  # Start the release (in ez-release).
  git tag -m "${tagMsg}" "${tagName}"
  gitPush origin tag "${tagName}"
}

function usage {
  cat >&2 <<HELP
Usage: $(basename "$0") relVer devVer
  relVer - release version, should match version in pom.xml
  devVer - development version, should be one higher than current pom.xml value

  Create a release branch and update the base for the next release.
  Both the relVer and devVer arguments are expected
HELP
}

function die {
  echo "$1" >&2
  usage
  exit 1
}

function main {
  if [ "--show" = "$1" ]; then
    showVersion
    exit 0
  fi

  if [ $# -eq 2 ]; then
    releaseAndPush "${1}" "${2}"
    exit 0
  fi

  die "$(basename "$0") requires relVer and a devVer"
}

main "$@"
