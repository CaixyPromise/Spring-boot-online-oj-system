

const { generateService } = require("@umijs/openapi");

generateService({
    requestLibPath: "import request from '@/lib/request'",
    schemaPath: "http://localhost:8100/api/auth/v2/api-docs",
    serversPath: "./api",
    projectName: "auth",
});