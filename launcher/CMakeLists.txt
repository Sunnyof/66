cmake_minimum_required(VERSION 3.8)

option(APP_NAME "Project Name" "Colorful interconnects")
project(${APP_NAME} CXX)
set(CC_LIB_NAME cocos)
set(CC_PROJECT_DIR ..)
set(CC_PROJ_SOURCES)
set(CC_COMMON_SOURCES)
set(CC_ALL_SOURCES)

include(../game/CMakeLists.txt)

cc_android_before_target(${CC_LIB_NAME})
add_library(${CC_LIB_NAME} SHARED ${CC_ALL_SOURCES})
cc_android_after_target(${CC_LIB_NAME})
