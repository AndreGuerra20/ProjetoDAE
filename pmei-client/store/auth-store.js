import { defineStore } from "pinia";
import {onBeforeMount} from "vue";

export const useAuthStore = defineStore("authStore", () => {
    const isClient = process.client;
    const token = ref(null);
    const user = ref(null);
    const role = ref(null);

    if (isClient) {
        console.log("Client side");
        token.value = sessionStorage.getItem("authToken");
        user.value = sessionStorage.getItem("authUser");
        role.value = sessionStorage.getItem("authRole");
    }

    function setToken(newToken) {
        token.value = newToken;
        sessionStorage.setItem("authToken", newToken);
    }

    function setUser(newUser) {
        user.value = newUser;
        sessionStorage.setItem("authUser", JSON.stringify(newUser));
    }

    function setRole(newRole) {
        role.value = newRole;
        sessionStorage.setItem("authRole", newRole);
    }

    function loadUser() {
        if (!isClient) {
            return;
        }
        user.value = sessionStorage.getItem("authUser");
        token.value = sessionStorage.getItem("authToken");
        role.value = sessionStorage.getItem("authRole");
    }

    function login(newToken, newUser) {
        setToken(newToken);
        setUser(newUser);
        setRole(newUser.role);
        console.log("Logged in as", newUser);
    }

    function logout() {
        if (!isClient) {
            return;
        }
        token.value = null;
        user.value = null;
        role.value = null;
        sessionStorage.removeItem("authToken");
        sessionStorage.removeItem("authUser");
        sessionStorage.removeItem("authRole");
    }

    function isAuthenticated() {
        return token.value !== null;
    }

    return { token, user, role, login, logout, isAuthenticated ,loadUser};
});
