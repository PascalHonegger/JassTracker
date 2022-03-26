import { createRouter, createWebHistory, RouteRecordRaw } from "vue-router";
import HomeView from "../views/HomeView.vue";

const routes: Array<RouteRecordRaw> = [
  {
    path: "/",
    name: "home",
    component: HomeView,
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
    component: () =>
      import(/* webpackChunkName: "overview" */ "../views/OverView.vue"),
  },
  {
    path: "/profile",
    name: "profile",
    component: () =>
      import(/* webpackChunkName: "profile" */ "../views/ProfileView.vue"),
  },
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
});

export default router;
