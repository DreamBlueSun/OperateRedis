//路径连接符
var nextPath = "/";
//获取项目路径
var pathName = document.location.pathname;
console.log(pathName)
var pathLength = pathName.substr(1).indexOf(nextPath) + 1;
console.log(pathLength)
var pathHead = pathName.substr(0, pathLength);