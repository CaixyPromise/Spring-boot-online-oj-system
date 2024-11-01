export default [
  { path: '/user', layout: false, routes: [{ path: '/user/login', component: './User/Login' }] },
  { path: '/welcome', icon: 'smile', component: './Welcome', name: "欢迎页" },
  {
    path: '/admin',
    icon: 'crown',
    name: "管理页",
    access: 'canAdmin',
    routes: [
      { path: '/admin', redirect: '/admin/user' },
      { icon: 'table', path: '/admin/user', component: './Admin/User', name: "用户管理" },
      { icon: 'table', path: '/admin/question', component: './Admin/Question', name: "题目管理"},
      { icon: 'table', path: '/admin/add_question', component: './Admin/AddQuestion', name: "添加题目"}

    ],
  },
  {
    path: "/account", icon: "user", component: "./User/Settings", name: "个人中心",
    routes: [
      { path: '/account/settings', exact: true, component: './User/Settings' },
      { path: '/account/settings/:operation', exact: true, component: './User/Settings' },
      { path: "/account/settings/", redirect: '/account/settings' }
    ]
  },
  { path: '/', redirect: '/welcome' },
  { path: '*', layout: false, component: './404' },
];
