# NATS Exporter

## Publish jar

Publishing the JAR is handled by a github workflow. 

### Republish jar with same tag

Short form, including amending existing commit:
```bash
git add . && git commit --no-edit --amend && git tag -d 1.0.0-ALPHA-1 && git tag 1.0.0-ALPHA-1 && git push --force --tags
```

Long form:
```bash
git tag -d 1.0.0-ALPHA-1
git tag 1.0.0-ALPHA-1
git push --force --tags
```