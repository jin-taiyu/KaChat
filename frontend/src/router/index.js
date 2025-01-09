import { createRouter, createWebHistory } from "vue-router";
import LoginView from "../views/LoginView.vue";
import RegisterView from "../views/RegisterView.vue";
import ChatView from "../views/ChatView.vue";

const routes = [
  { path: "/", redirect: "/login", meta: { title: "登录 - KaChat" } },
  {
    path: "/login",
    name: "login",
    component: LoginView,
    meta: { title: "登录 - KaChat" },
  },
  {
    path: "/register",
    name: "register",
    component: RegisterView,
    meta: { title: "注册 - KaChat" },
  },
  {
    path: "/chat",
    name: "chat",
    component: ChatView,
    meta: { title: "聊天 - KaChat" },
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

router.beforeEach((to, from, next) => {
  document.title = to.meta.title || "KaChat";
  next();
});

export default router;
