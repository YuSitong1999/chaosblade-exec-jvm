
通过 IDE 执行

```shell
/usr/lib/jvm/java-1.8.0-openjdk-amd64/bin/java \
  -Dmaven.multiModuleProjectDirectory=/root/dev-chaosblade/chaosblade-exec-jvm \
  -Djansi.passthrough=true \
  -Dmaven.home=/root/.cache/JetBrains/RemoteDev/dist/d5c52f40ba10d_ideaIU-2023.1.3/plugins/maven/lib/maven3 \
  -Dclassworlds.conf=/root/.cache/JetBrains/RemoteDev/dist/d5c52f40ba10d_ideaIU-2023.1.3/plugins/maven/lib/maven3/bin/m2.conf \
  -Dmaven.ext.class.path=/root/.cache/JetBrains/RemoteDev/dist/d5c52f40ba10d_ideaIU-2023.1.3/plugins/maven/lib/maven-event-listener.jar \
  -javaagent:/root/.cache/JetBrains/RemoteDev/dist/d5c52f40ba10d_ideaIU-2023.1.3/lib/idea_rt.jar=37471:/root/.cache/JetBrains/RemoteDev/dist/d5c52f40ba10d_ideaIU-2023.1.3/bin \
  -Dfile.encoding=UTF-8 \
  -classpath /root/.cache/JetBrains/RemoteDev/dist/d5c52f40ba10d_ideaIU-2023.1.3/plugins/maven/lib/maven3/boot/plexus-classworlds-2.6.0.jar:/root/.cache/JetBrains/RemoteDev/dist/d5c52f40ba10d_ideaIU-2023.1.3/plugins/maven/lib/maven3/boot/plexus-classworlds.license org.codehaus.classworlds.Launcher \
  -Didea.version=2023.1.3 \
  -Dmaven.test.skip=true \
  org.apache.maven.plugins:maven-assembly-plugin:2.2-beta-5:assembly
```

修改 pom.xml 后

```shell
mvn clean package -Dmaven.test.skip=true -U
```
