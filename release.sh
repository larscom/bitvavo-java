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

echo "Updating version in README.md to $VERSION"
sed -i "s|\(<version>\)[^<]*\(</version>\)|\1$VERSION\2|" README.md

git add pom.xml README.md
git commit -m "prepared for release version $VERSION"

git tag -a "$TAG" -m "Release $TAG"

git push --follow-tags

echo "✔ Tag $TAG created, README.md updated, and everything pushed successfully."
