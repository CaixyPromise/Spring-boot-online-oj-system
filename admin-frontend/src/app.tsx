import Footer from '@/components/Footer';
import type { RunTimeLayoutConfig } from '@umijs/max';
import { history } from '@umijs/max';
import defaultSettings from '../config/defaultSettings';
import { AvatarDropdown } from './components/RightContent/AvatarDropdown';
import { requestConfig } from './requestConfig';
import {InitialState} from "@/typings";
import {getLoginMeUsingGet1} from "@/services/onlineJudge/authBackend/authController";
import {ProLayoutProps} from "@ant-design/pro-components";

const loginPath = '/user/login';

/**
 * @see  https://umijs.org/zh-CN/plugins/plugin-initial-state
 * */
export async function getInitialState(): Promise<InitialState> {

  const fetchUserInfo = async (): Promise<API.LoginUserVO | undefined> => {
    try {
      const res = await getLoginMeUsingGet1();
      return res.data as API.UserVO;
    } catch (error: any) {
      // 如果未登录
      window.location.href = loginPath;
      return undefined;
    }
  }
  const initialState: InitialState = {
    currentUser: undefined,
    settings: defaultSettings as ProLayoutProps,
    fetchUserInfo
  };
  // 如果不是登录页面，执行
  const { location } = history;
  if (location.pathname !== loginPath) {
    return {
      ...initialState,
      currentUser: await fetchUserInfo(),
    }
  }
  return initialState;
}

// ProLayout 支持的api https://procomponents.ant.design/components/layout
// @ts-ignore
export const layout: RunTimeLayoutConfig = ({ initialState }) => {
  return {
    avatarProps: {
      render: () => {
        return <AvatarDropdown menu={true} />;
      },
    },
    waterMarkProps: {
      content: initialState?.currentUser?.nickName,
    },
    footerRender: () => <Footer />,
    menuHeaderRender: undefined,
    // 自定义 403 页面
    // unAccessible: <div>unAccessible</div>,
    ...defaultSettings,
  };
};

/**
 * @name request 配置，可以配置错误处理
 * 它基于 axios 和 ahooks 的 useRequest 提供了一套统一的网络请求和错误处理方案。
 * @doc https://umijs.org/docs/max/request#配置
 */
export const request = requestConfig;
