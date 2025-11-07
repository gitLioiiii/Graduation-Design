// 通用 URL 构建
export const buildURL = (filename) =>new URL(filename,import.meta.env.APP_BASE_URL).href

// 用户头像 - 后端返回格式：/avatars/xxx.png
export const buildAvatarURL = (filename) => buildURL(filename)

// 门票图片 - 后端返回格式：/tickets/xxx.png
export const buildTicketURL = (filename) => buildURL(filename)
