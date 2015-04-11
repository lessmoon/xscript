## XXXScript in brief ##

### Keyword List: ###
<table>
	<tr>
        <td>_file_</td><td>_line_</td>
		<td>_version_</td><td>bool</td>
		<td>break</td><td>char</td>
	</tr>
	<tr>
		<td>continue</td><td>def</td>
		<td>do</td><td>else</td>
		<td>false</td><td>for</td>
	</tr>
	<tr>
		<td>if</td><td>import</td>
		<td>int</td><td>loadfunc</td>
		<td>new</td><td>real</td>
	</tr>
	<tr>
		<td>return</td><td>sizeof</td>
		<td>string</td><td>struct</td>
		<td>this</td><td>true</td>
	</tr>
	<tr>
		<td>while</td><td>while</td>
	</tr>
</table>

### Basic Types: ###
<table>
		<tr>
			<th>name</th>
			<th>size</th>
			<th>name</th>
			<th>size</th>
		</tr>
		<tr>
			<td>char</td>
			<td>2</td>
			<td>int</td>
			<td>4</td>
		</tr>
		<tr>
			<td>real</td>
			<td>4</td>
			<td>string</td>
			<td>x</td>
		</tr>
		<tr>
			<td>bool</td>
			<td>1</td>
		<tr>
</table>

### Grammar ###

* Loop statements:
	* `while`(bool-type expression) statement
	* `for`(def-expression;bool-type expression;expression) statement
	* `do` statement `while`(bool-type expression);
* Condition statement
	* `if`(bool-type expression) statement
	* `if`(bool-type expression) `else` statement
* Jump statement
	* `break`(or `continue`);(in loops)
	* `return` expression;(in functions)
* Function declaration & defination
	* Defination
		* Normal dunction defination
			1. `def` return-type func-name ( [para-type para-identifier,...] ){
			2. &emsp;&emsp; funtion-body
			3. }
		* Structure's member function defination(can use `this` variable in function body)
			1. `def` return-type structure-name.func-name ([para-type para-`,...]){
			2. &emsp;&emsp;function-body
 			3. }
	* Declaration(if not used,no need to implement it)
		- `def` return-type func-name([para-type para-name,...]);
* Structure declaration & definition :
	- Define a structure
		1.  `struct` name {
		2. &emsp;type-name var-name;[...] ***//Member declaration***
		3. &emsp;`def` return-type func-name([para-type para-name,...]);***//Function declaration***
		4. &emsp;`def` return-type func-name([para-type para-name,...]){***//Function defination***
		5. &emsp;&emsp;&emsp;&emsp;function-body;
		6. &emsp;&nbsp;}
		7. &nbsp;}
	- Structure member access:
		- name.member-name
* Load extension function:
	1. loadfunc<package-name>{
	2. &emsp;return-type func-name ( [para-type para-name,...] );
	3. }
* Import source file:
	- `import` "file-path";
* Expressions:
	* variable `+=` expression(variable's type can be `string`)
	* variable (`=`,`-=`,`*=`,`/=`,`%=`) expression(`%=` only for `int` & `char`)
	* cast expression
		- ( `type-name` ) expression
	* string/array access:
		- `var-expression` [expression]...
	* unary expression:
		- prefix:`-`,`!`,`++`,`--`
		- postfix:`++`,`--`
	* expression (`+`,`-`,`*`,`/`,`%`) expression(`%` for `int` & `char`)
		- expression (`==`,`!=`,`>`,`>=`,`<`,`<=`) expression
		- bool-type expression (`&&`,`||`) bool-type  expression
	* Variable declaration
		- Basic-type var-name[,...] (var-name must begin with one of [a-zA-Z_])
		- Basic-type var-name = initial-value[,...](with initial-value)
		- Basic-type [integer constant]... var-name[,...](for array declaration)
	* Constant:
		- `int`:only decimal integer is supported
		- `char`:'character'(character includes escape character `\n`,`\t`,`\r`,`\'`,`\"`,`\?`,`\b`,`\f`,`\\`)
		- `string`:"characters"
		- `real`:just support decimal real number
		- `bool`:`true`,`false`
	* Dynamic array allocation:
		- `new` < type > (expression);
	* Array size getter:
		- `sizeof` array-type-expression
	* Build-in variable:
		- `_line_`:  line number of the source file
		- `_file_`:  file name of the source file
		- `_version_`: compiler version(major-version * 100 + minor-version)
* Features fn future
	* Inline function definition:
		- `def` return-type func-name([para-type para-name,...]) = expression;