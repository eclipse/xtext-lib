mvn \
  -f releng/org.eclipse.xtext.dev-bom \
  --batch-mode \
  --update-snapshots \
  -Dmaven.repo.local=.m2/repository \
  -DJENKINS_URL=https://ci.eclipse.org/xtext/ \
  -Dorg.slf4j.simpleLogger.log.org.apache.maven.cli.transfer.Slf4jMavenTransferListener=warn \
  clean install
