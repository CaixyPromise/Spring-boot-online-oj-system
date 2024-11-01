import {getActiveTokenUsingGet1} from "@/api/auth/tokenController";

export const queryServer = {
    getSearchToken: async (token: string) => {
        const {data, response} = await getActiveTokenUsingGet1({
            token
        })
    }
}