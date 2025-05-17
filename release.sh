#!/usr/bin/env bash
set -euo pipefail

git fetch --tags

VERSION=$(xmlstarlet sel -N x="http://maven.apache.org/POM/4.0.0" \
  -t -v "/x:project/x:version" pom.xml)

if [[ -z "$VERSION" ]]; then
  echo "Error: Could not extract version from pom.xml"
  exit 1
fi

TAG="$VERSION"

if git tag --list | grep -q "^$TAG$"; then
  echo "Error: Tag $TAG already exists."
  exit 1
fi

git tag -a "$TAG" -m "Release $TAG"

git push --follow-tags

echo "Tag $TAG created and pushed successfully."