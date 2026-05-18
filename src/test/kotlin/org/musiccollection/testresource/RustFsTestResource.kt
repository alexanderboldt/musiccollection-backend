package org.musiccollection.testresource

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager
import org.testcontainers.containers.GenericContainer
import org.testcontainers.utility.DockerImageName

class RustFsTestResource: QuarkusTestResourceLifecycleManager {

    private val rustfs = GenericContainer(DockerImageName.parse("rustfs/rustfs:latest"))
        .withExposedPorts(9000)
        .withEnv("RUSTFS_ACCESS_KEY", "testtest")
        .withEnv("RUSTFS_SECRET_KEY", "testtest")

    override fun start(): Map<String, String> {
        rustfs.start()

        val host = rustfs.host
        val port = rustfs.getMappedPort(9000)
        val endpoint = "http://$host:$port"

        return buildMap {
            put("quarkus.s3.endpoint-override", endpoint)
            put("quarkus.s3.aws.credentials.static-provider.access-key-id", "testtest")
            put("quarkus.s3.aws.credentials.static-provider.secret-access-key", "testtest")
        }
    }

    override fun stop() {
        rustfs.stop()
    }
}
