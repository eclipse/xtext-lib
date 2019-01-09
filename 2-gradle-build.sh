./gradlew \
  clean cleanGenerateXtext build createLocalMavenRepo \
  -Dmaven.repo.local=$(pwd)/.m2/repository \
  -PcompileXtend=true \
  -PJENKINS_URL=https://ci.eclipse.org/xtext/ \
  -PignoreTestFailures=true \
  --refresh-dependencies \
  --continue
