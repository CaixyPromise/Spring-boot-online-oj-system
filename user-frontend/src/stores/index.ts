import { configureStore } from "@reduxjs/toolkit";
import LoginUser from "@/stores/LoginUser/index";

const store = configureStore({
    reducer:{
        LoginUser,
    }
})

export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;

export default store;