import 'codemirror/lib/codemirror.css'
// require active-line.js
import 'codemirror/addon/selection/active-line.js'
// styleSelectedText
import 'codemirror/addon/selection/mark-selection.js'
// hint
import 'codemirror/addon/hint/show-hint.js'
import 'codemirror/addon/hint/sql-hint.js'
import 'codemirror/addon/hint/show-hint.css'
import 'codemirror/addon/hint/javascript-hint.js'
// highlightSelectionMatches
import 'codemirror/addon/scroll/annotatescrollbar.js'
import 'codemirror/addon/search/matchesonscrollbar.js'
import 'codemirror/addon/search/match-highlighter.js'
// keyMap
import 'codemirror/mode/clike/clike.js'
import 'codemirror/mode/sql/sql.js'
import 'codemirror/addon/edit/matchbrackets.js'
import 'codemirror/addon/comment/comment.js'
import 'codemirror/addon/dialog/dialog.js'
import 'codemirror/addon/dialog/dialog.css'
import 'codemirror/addon/search/searchcursor.js'
import 'codemirror/addon/search/search.js'
import 'codemirror/keymap/sublime.js'
import 'codemirror/keymap/vim.js'
// foldGutter
import 'codemirror/addon/fold/foldgutter.css'
import 'codemirror/addon/fold/brace-fold.js'
import 'codemirror/addon/fold/comment-fold.js'
import 'codemirror/addon/fold/foldcode.js'
import 'codemirror/addon/fold/foldgutter.js'
import 'codemirror/addon/fold/indent-fold.js'
import 'codemirror/addon/fold/markdown-fold.js'
import 'codemirror/addon/fold/xml-fold.js'
// 编辑的主题文件
import 'codemirror/theme/monokai.css'
import 'codemirror/theme/mdn-like.css';
// 编辑器代码格式
import 'codemirror/mode/properties/properties.js'
import 'codemirror/mode/spreadsheet/spreadsheet.js'
import 'codemirror/mode/crystal/crystal.js'
import 'codemirror/mode/brainfuck/brainfuck.js'
import 'codemirror/mode/clojure/clojure.js'
import 'codemirror/mode/cmake/cmake.js'
import 'codemirror/mode/cobol/cobol.js'
import 'codemirror/mode/coffeescript/coffeescript.js'

/**
 * ================================================
 */
// 需要引入具体的语法高亮库才会有对应的语法高亮效果
// codemirror 官方其实支持通过 /addon/mode/loadmode.js 和 /mode/meta.js 来实现动态加载对应语法高亮库
// 但 vue 貌似没有无法在实例初始化后再动态加载对应 JS ，所以此处才把对应的 JS 提前引入
// import 'codemirror/mode/javascript/javascript.js';
// import 'codemirror/mode/css/css.js';
// import 'codemirror/mode/xml/xml.js';
// import 'codemirror/mode/clike/clike.js';
// import 'codemirror/mode/markdown/markdown.js';
// import 'codemirror/mode/python/python.js';
// import 'codemirror/mode/r/r.js';
// import 'codemirror/mode/shell/shell.js';
// import 'codemirror/mode/sql/sql.js';
// import 'codemirror/mode/swift/swift.js';
// import 'codemirror/mode/vue/vue.js';
// import 'codemirror/mode/htmlembedded/htmlembedded.js'
// // 编辑器代码格式
// import 'codemirror/mode/properties/properties.js'
// import 'codemirror/mode/spreadsheet/spreadsheet.js'
// import 'codemirror/mode/crystal/crystal.js'
// import 'codemirror/mode/brainfuck/brainfuck.js'
// import 'codemirror/mode/clojure/clojure.js'
// import 'codemirror/mode/cmake/cmake.js'
// import 'codemirror/mode/cobol/cobol.js'
// import 'codemirror/mode/coffeescript/coffeescript.js'
// // 引入代码块主题  我这边是动态主题，所以引入的有点多
// // 正常下面设置 theme: '3024-day' 就好了
// import 'codemirror/theme/mdn-like.css';

// // 自动刷新(防止codemirror需要手动刷新才出数据)
// import 'codemirror/addon/display/autorefresh'
// import 'codemirror/addon/edit/closebrackets.js'
// import 'codemirror/addon/edit/closetag.js'
// import "codemirror/addon/edit/matchbrackets";// 括号显示匹配
// import "codemirror/addon/edit/matchtags";// 括号显示匹配
// import 'codemirror/addon/scroll/simplescrollbars.css'
// import 'codemirror/addon/scroll/simplescrollbars'
// import 'codemirror/addon/hint/show-hint'
// import 'codemirror/addon/hint/javascript-hint'
// import 'codemirror/addon/hint/sql-hint'
// import 'codemirror/addon/hint/html-hint'
// import 'codemirror/addon/hint/xml-hint'
// import 'codemirror/addon/hint/anyword-hint'
// import 'codemirror/addon/hint/css-hint'
// import 'codemirror/addon/hint/show-hint.css'
// import 'codemirror/addon/fold/markdown-fold'
// import "codemirror/addon/fold/foldgutter.css"
// import "codemirror/addon/fold/foldcode"
// import "codemirror/addon/fold/brace-fold"
// import 'codemirror/addon/selection/active-line'
// import 'codemirror/addon/selection/selection-pointer'
// import 'codemirror/addon/mode/overlay'
// import 'codemirror/addon/mode/multiplex'