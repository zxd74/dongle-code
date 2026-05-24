# C实现
[参考源](https://gitee.com/jk-never/snake-game)

* `snake.h` 头文件
```h
#define _CRT_SECURE_NO_WARNINGS 1

#include <stdio.h>
#include <stdlib.h>
#include <conio.h>
#include <windows.h>
#include <time.h>
#include <string.h>

// 定义方向常量
#define UP 1
#define DOWN 2
#define LEFT 3
#define RIGHT 4

// 定义游戏区域大小
#define WIDTH 20
#define HEIGHT 20

// 单链表定义蛇节点结构
typedef struct SnakeNode {
    int x; // 蛇头或蛇身的x坐标
    int y; // 蛇头或蛇身的y坐标
    struct SnakeNode* next; // 指向下一个节点的指针
} SnakeNode;
// 定义蛇结构
typedef struct Snake {
    SnakeNode* head; // 蛇头指针
    SnakeNode* tail; // 蛇尾指针
    int length; // 蛇的长度
    int direction; // 当前移动方向
} Snake;

// 定义游戏状态结构
typedef struct GameState {
    int foodX; // 食物的x坐标
    int foodY; // 食物的y坐标
    int score; // 当前得分
    int gameOver; // 游戏结束标志
} GameState;

// 蛇的初始化和管理函数
void initSnake(Snake* snake);
void freeSnake(Snake* snake);
void moveSnake(Snake* snake, GameState* state);
void resetSnake(Snake* snake);

// 游戏状态管理函数
void initGameState(GameState* state);
void resetGameState(GameState* state);
void generateFood(Snake* snake,GameState* state);

// 游戏界面函数
void draw(Snake* snake, GameState* state);
void input(Snake* snake);

// 工具函数
int checkCollision(Snake* snake);
void clearInputBuffer();
```
* `snake.c` 逻辑实现
```c

#include "snake.h"

void initSnake(Snake* snake) {
    // 初始化蛇，初始长度为1，位置在游戏区域中心
    snake->head = (SnakeNode*)malloc(sizeof(SnakeNode));
    snake->head->x = WIDTH / 2;
    snake->head->y = HEIGHT / 2;
    snake->head->next = NULL;
    snake->tail = snake->head;
    snake->length = 1;
    snake->direction = RIGHT; // 初始方向向右
}

void freeSnake(Snake* snake) {
    // 释放蛇的内存
    SnakeNode* current = snake->head;
    while (current!=NULL) {
        SnakeNode* temp = current;
        current = current->next;
        free(temp);
    }
    snake->head = NULL;
    snake->tail = NULL;
    snake->length = 0;
}

void resetSnake(Snake* snake) {
    // 重置蛇到初始状态
    freeSnake(snake);
    initSnake(snake);
}


void initGameState(GameState* state) {
    // 初始化游戏状态
    state->score = 0;
    state->gameOver = 0;
    state->foodX = 0;
    state->foodY = 0;
}

void resetGameState(GameState* state) {
    // 重置游戏状态
    state->score = 0;
    state->gameOver = 0;
}

void generateFood(Snake* snake, GameState* state) {
    // 生成食物，确保食物不出现在蛇身上
    int valid;
    do {
        valid = 1; // 假设位置有效
        //使用随机数生成食物坐标，范围在[1, WIDTH-2]和[1, HEIGHT-2]之间
        state->foodX = rand() % (WIDTH - 2) + 1;;
        state->foodY = rand() % (HEIGHT - 2) + 1;;
        // 检查食物位置是否与蛇身重叠
        SnakeNode* current = snake->head;
        while (current != NULL) {
            if (current->x == state->foodX && current->y == state->foodY) {
                valid = 0; // 食物位置与蛇身重叠，重新生成
                break;
            }
            current = current->next;
        }
    } while (!valid);
}

void draw(Snake* snake, GameState* state) {
    // 绘制游戏界面
    system("cls"); // 清屏

    for (size_t i = 0; i < WIDTH+2; i++)
    {
        printf("#");
    }
    printf("\n");
    

    for (int y = 0; y < HEIGHT; y++) {
        for (int x = 0; x < WIDTH; x++) {
            if (x == 0 || x == WIDTH - 1 || y == 0 || y == HEIGHT - 1) {
                printf("#"); // 边界
            } else if (x == state->foodX && y == state->foodY) {
                printf("*"); // 食物
            } else {
                int isSnakePart = 0;
                SnakeNode* current = snake->head;
                while (current != NULL) {
                    if (current->x == x && current->y == y) {
                        printf("O"); // 蛇身
                        isSnakePart = 1;
                        break;
                    }
                    current = current->next;
                }
                if (!isSnakePart) {
                    printf(" "); // 空格
                }
            }
        }
        printf("\n");
    }
    printf("Score: %d\n", state->score);
}

void moveSnake(Snake* snake, GameState* state) {
    // 根据当前方向移动蛇
    int newX = snake->head->x;
    int newY = snake->head->y;

    switch (snake->direction) {
        case UP: newY--; break;
        case DOWN: newY++; break;
        case LEFT: newX--; break;
        case RIGHT: newX++; break;
    }

    // 创建新的蛇头
    SnakeNode* newHead = (SnakeNode*)malloc(sizeof(SnakeNode));
    newHead->x = newX;
    newHead->y = newY;
    newHead->next = snake->head;
    snake->head = newHead;
    snake->length++;

    // 检查是否吃到食物
    if (newX == state->foodX && newY == state->foodY) {
        state->score += 10; // 增加得分
        generateFood(snake, state); // 生成新的食物
    } else {
        // 没有吃到食物，移除蛇尾
        SnakeNode* temp = snake->tail;
        snake->tail = snake->tail->next;
        free(temp);
        snake->length--;
    }

    state->gameOver = checkCollision(snake); // 检查碰撞
}

void input(Snake* snake) {
    // 处理用户输入，改变蛇的移动方向
    if (_kbhit()) {
        char ch = _getch();
        switch (ch) {
            case 'w': 
            case 'W': if (snake->direction != DOWN) snake->direction = UP; break;
            case 's': 
            case 'S': if (snake->direction != UP) snake->direction = DOWN; break;
            case 'a': 
            case 'A': if (snake->direction != RIGHT) snake->direction = LEFT; break;
            case 'd': 
            case 'D': if (snake->direction != LEFT) snake->direction = RIGHT; break;
            case 27: // ESC键退出
                snake->direction = 0; // 设置一个特殊值表示退出
                break;
        }
    }
}

int checkCollision(Snake* snake) {
    // 检查蛇是否碰撞到自己或边界
    if (snake->head->x <= 0 || snake->head->x >= WIDTH - 1 || 
        snake->head->y <= 0 || snake->head->y >= HEIGHT - 1) {
        return 1; // 碰撞到边界
    }
    SnakeNode* current = snake->head->next;
    while (current != NULL) {
        if (snake->head->x == current->x && snake->head->y == current->y) {
            return 1; // 碰撞到自己
        }
        current = current->next;
    }
    return 0; // 没有碰撞
}

void clearInputBuffer() {
    // 清除输入缓冲区，防止多次输入
    while (_kbhit()) {
        _getch();
    }
}
```
* `game.c` 游戏管理
```c
#include "snake.h"

int showWelcomeScreen() {
    char input[20];
    //清除屏幕
    system("cls");
    printf("=====================================\n");
    printf("           贪吃蛇游戏\n");
    printf("           单链表实现\n");
    printf("=====================================\n");
    printf("游戏控制:\n");
    printf("  W - 向上移动\n");
    printf("  S - 向下移动\n");
    printf("  A - 向左移动\n");
    printf("  D - 向右移动\n");
    printf("  X 或 ESC - 退出游戏\n");
    printf("=====================================\n");

    while (1) {
        printf("请输入 'start' 开始游戏: ");

        // 清空输入缓冲区
        clearInputBuffer();

        // 读取用户输入
        if (scanf("%s", input) == 1) {
            // 比较输入是否为"start"
            if (_stricmp(input, "start") == 0) {
                return 1;  // 开始游戏
            }
            else {
                printf("输入错误！请重新输入。\n");
            }
        }
    }
}

int showGameOverScreen(int score) {
    int choice;

    printf("=====================================\n");
    printf("           游戏结束!\n");
    printf("           最终得分: %d\n", score);
    printf("=====================================\n");
    printf("请选择:\n");
    printf("  0 - 重新开始游戏\n");
    printf("  1 - 退出游戏\n");
    printf("请输入您的选择 (0 或 1): ");

    while (1) {
        // 清空输入缓冲区
        clearInputBuffer();

        // 读取用户选择
        if (scanf("%d", &choice) == 1) {
            if (choice == 0 || choice == 1) {
                return choice;
            }
            else {
                printf("输入无效！请输入 0 或 1: ");
            }
        }
        else {
            printf("输入无效！请输入 0 或 1: ");
            // 清空错误的输入
            clearInputBuffer();
        }
    }
}

void runGame(Snake* snake, GameState* gameState) {
    // 游戏主循环
    while (!gameState->gameOver) {
        draw(snake, gameState);
        input(snake);
        moveSnake(snake, gameState);

        // 检查用户是否主动退出
        if (_kbhit()) {
            char ch = _getch();
            if (ch == 'x' || ch == 'X' || ch == 27) {  // X 或 ESC
                gameState->gameOver = 1;
                break;
            }
        }

        Sleep(200);  // 控制游戏速度 (200毫秒)
    }
}

int main(){
    srand((unsigned int)time(NULL));  // 初始化随机数种子

    Snake snake;
    GameState gameState;
    int playAgain = 1;

    // 显示欢迎信息并等待用户输入start
    if (showWelcomeMessage()) {

        do {
            // 初始化游戏
            initSnake(&snake);
            initGameState(&gameState);
            generateFood(&snake, &gameState);

            // 运行游戏
            runGame(&snake, &gameState);

            // 显示游戏结束信息并获取用户选择
            int choice = showGameOverMessage(gameState.score);

            if (choice == 0) {
                // 用户选择重新开始游戏
                playAgain = 1;
                printf("重新开始游戏...\n");
                Sleep(1000);  // 暂停1秒让用户看到消息
            }
            else {
                // 用户选择退出游戏
                playAgain = 0;
            }

        } while (playAgain);
    }

    // 释放内存
    freeSnake(&snake);

    printf("感谢游玩！再见！\n");
    Sleep(1000);  // 暂停1秒

    return 0;
}
```