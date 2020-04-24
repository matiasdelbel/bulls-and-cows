include (":app")

include (":game:domain")
include (":game:gateway")
include (":game:presentation")

project(":game:domain").projectDir = File(rootDir, "game/domain")
project(":game:gateway").projectDir = File(rootDir, "game/gateway")
project(":game:presentation").projectDir = File(rootDir, "game/presentation")

include (":session:domain")
include (":session:gateway")
include (":session:presentation")

project(":session:domain").projectDir = File(rootDir, "session/domain")
project(":session:gateway").projectDir = File(rootDir, "session/gateway")
project(":session:presentation").projectDir = File(rootDir, "session/presentation")