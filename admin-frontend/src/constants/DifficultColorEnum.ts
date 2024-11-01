export const DifficultColorEnum = {
    EASY: '#00AF9B',
    MEDIUM: '#FFB800',
    HARD: '#FF2D55',
    DEFAULT: '#555353'
}
export const DifficultyMapper= (hard: number | undefined) => {
    switch (hard) {
        case 1:
            return {
                color: DifficultColorEnum.EASY,
                text: '简单'
            };
        case 2:
            return {
                color: DifficultColorEnum.MEDIUM,
                text: '中等'
            }
        case 3:
            return {
                color: DifficultColorEnum.HARD,
                text: '困难'
            }
        default:
            return {
                color: DifficultColorEnum.DEFAULT,
                text: '未知'
            }
    }
}