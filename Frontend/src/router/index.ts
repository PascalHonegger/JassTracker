import { createRouter, createWebHistory, RouteRecordRaw } from "vue-router";
import LoginView from "../views/LoginView.vue";
import NotFoundView from "../views/NotFoundView.vue";
import { useAuthStore } from "@/store/auth-store";

const routes: Array<RouteRecordRaw> = [
  {
    path: "/",
    redirect: "/overview",
  },
  {
    path: "/login",
    name: "login",
    component: LoginView,
  },
  {
    path: "/help",
    name: "help",
    component: () =>
      import(/* webpackChunkName: "help" */ "../views/HelpView.vue"),
  },
  {
    path: "/overview",
    name: "overview",
    meta: {
      requiresAuth: true,
    },
    component: () =>
      import(/* webpackChunkName: "overview" */ "../views/OverView.vue"),
  },
  {
    path: "/profile",
    name: "profile",
    meta: {
      requiresAuth: true,
    },
    component: () =>
      import(/* webpackChunkName: "profile" */ "../views/ProfileView.vue"),
  },
  {
    path: "/table/:id",
    name: "table",
    meta: {
      requiresAuth: true,
    },
    component: () =>
      import(/* webpackChunkName: "detail" */ "../views/TableDetailView.vue"),
  },
  {
    path: "/table/:tableId/game/:gameId",
    name: "game",
    meta: {
      requiresAuth: true,
    },
    component: () => import("../views/GameDetailView.vue"),
  },
  {
    path: "/:pathMatch(.*)*",
    name: "NotFound",
    meta: {
      requiresAuth: true,
    },
    component: NotFoundView,
  },
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
});

router.beforeEach((to, from, next) => {
  if (to.matched.some((record) => record.meta.requiresAuth)) {
    if (!useAuthStore().loggedIn) {
      next({ name: "login" });
      return;
    }
  }
  next();
});

export default router;
