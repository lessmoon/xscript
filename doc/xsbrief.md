## XXXScript in brief ##

### Keyword List: ###
<table>
<tr><td>_file_</td><td>_line_</td><td>_version_</td><td>bigint</td><td>bigreal</td></tr>
<tr><td>bool</td><td>break</td><td>case</td><td>char</td><td>continue</td></tr>
<tr><td>def</td><td>default</td><td>do</td><td>else</td><td>false</td></tr>
<tr><td>for</td><td>if</td><td>import</td><td>instanceof</td><td>int</td></tr>
<tr><td>native</td><td>new</td><td>null</td><td>override</td><td>real</td></tr>
<tr><td>return</td><td>sizeof</td><td>string</td><td>struct</td><td>super</td></tr>
<tr><td>switch</td><td>this</td><td>true</td><td>virtual</td><td>while</td></tr>
<tr></tr>
</table>


### Basic Types: ###
<table>
		<tr>
			<th>name</th>
			<th>size</th>
			<th>init</th>
			<th>name</th>
			<th>size</th>
			<th>init</th>
		</tr>
		<tr>
			<td>char</td>
			<td>2</td>
			<td>'\0'</td>
			<td>string</td>
			<td>x</td>
			<td>""</td>
		</tr>
		<tr>
			<td>int</td>
			<td>4</td>
			<td>0i</td>
			<td>real</td>
			<td>4</td>
			<td>0.0r</td>
		</tr>
		<tr>
			<td>bigint</td>
			<td>x</td>
			<td>0I</td>
			<td>bigreal</td>
			<td>x</td>
			<td>0.0R</td>
		</tr>
		<tr>
			<td>bool</td>
			<td>1</td>
			<td>false</td>
			<td></td>
			<td></td>
			<td></td>
		</tr>
		<tr>
			<td>struct</td>
			<td>x</td>
			<td>null</td>
			<td>array</td>
			<td>x</td>
			<td>null</td>
		</tr>
</table>

### Grammar ###

* Loop statements:
	* `while`(bool-type expression) statement
	* `for`(def-expression;bool-type expression;expression) statement
	* `do` statement `while`(bool-type expression);
* Condition statement
	* `if`(bool-type expression) statement
	* `if`(bool-type expression) `else` statement
	*  switch statement
		1. `switch`(expression){**expression should be int,char or string**
		2. &emsp;`case` constant-expression:**expression should be constant**
		3. &emsp;&emsp;case-body;**use `break` if jumping out is needed**
		3. &emsp;`default`:**default case**
		4. &emsp;&emsp;case-body;
		4. }
	  
* Jump statement
	* `break`;(in loops or switch)
	* `continue`;(in loops)
	* `return` expression;(in functions)
* Function declaration & definition
	* Definition
		* Normal function definition
			1. `def` return-type func-name ( [param-type param-name,...] ){
			2. &emsp;&emsp; function-body
			3. }
		* Struct's member function definition(can use `this` variable in function body)
			1. `def` return-type structure-name.func-name ([param-type param-name,...]){
			2. &emsp;&emsp;function-body
 			3. }
 		* Struct's initial function definition(super() function to initial base struct)
	 		1. `def` structure-name.`this`([param-type param-name,...]){
	 		2. &emsp;&emsp;function-body
	 		3. }
	* Declaration(if not used,no need to implement it)
		- `def` return-type func-name([param-type param-name,...]);
* Structure declaration & definition :
	- Define a structure
		1.  `struct` name;***pre-declaration***
		2.  `struct` name:base-name; **pre-declaration with base struct**
		3.  `struct` name {
		4. &emsp;type-name var-name;[...] **Member variable declaration**
		7. ***Initial function declaration***
		5. &emsp;`def` `this`([param-type param-name,...]);
		6. ***Initial function definition(can just have one for each struct now )***
		6. &emsp;`def` `this`([param-type param-name,...]){
		7. &emsp;&emsp;&emsp;&emsp;function-body;
		8. &emsp;}
		9. ***Function declaration***
		9. &emsp;`def` return-type func-name([param-type param-name,...]);
		10. ***Function definition***
		10. &emsp;`def` return-type func-name([param-type param-name,...]){
		11. &emsp;&emsp;&emsp;&emsp;function-body;
		12.	&emsp;&nbsp;}
		13. &nbsp;}
	- Inheriting and overriding
		1. Inherit a base structure
			- `struct` **derive-name**`:`**base-name**
		2. Virtual function
			- `def` [`default`] `virtual` return-type func-name([param-type param-name,...]) 
		3. Override function
			- `def` [`default`] `override` return-type func-name([param-type param-name,...]) 
		4. Override function must be virtual function in base struct
		5. If a struct(or its base struct) declared a virtual function but no definition,it can't be instantiated
		6. Initial function shouldn't be `virtual`,and so it can't use `override`
	- Operand overloading
		- Definition grammar
			1.  `struct` name {
			2.  &emsp;@operand-name
			4.  &emsp;***Function declaration or definition***
			3.  &emsp;`def` return-type func-name([param-type param-name,...]);
			4.  &nbsp;}
		- Constraint
			1. available operands have `+`,`-`,`*`,`/`,`%`,`>`,`<`,`<=`,`>=`, and types(except the self-type and array)
			2. operands `+`,`-`,`*`,`*` overloading should&only have 1 parameter whose type is the struct,
			and return this struct type
			3. operands `<`,`>`,`<=`,`>=` overloading should&only have 1 parameter whose type is the struct,
			and return-type should be `bool`
			4. the operands `==`,`!=` is a built-in operation,they compared by reference,
			disable their overloading to avoid confusion
			5. type-conversion function should have no parameter and its return-type should be the same as the operand
			6. unfortunately initial functions can't overload operands
	- Structure member access:
		- name.member-name
		- name.func-name([argument,...])
		- name can be `this` and `super`,but with `super`,it must have a base struct and this base struct must 
		have implemented this member(function or variable).
* Load extension
	1. native\<package-name\>{
	2. &emsp;["class-name":]return-type func-name ( [param-type param-name,...] );***load extension functions***
	3. &emsp;["class-name":]struct struct-name; ***extension struct pre-declaration***
	4. &emsp;["class-name":]struct struct-name{
	4. &emsp;&emsp;def this(param-list);***function declarations(not necessary,but check the correctness)***
	4. &emsp;};
	5. }
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
	* Anonymous inner struct
		* Syntax:
			1.  `new` struct-type(arguments){
			2.  &emsp;&emsp;type var;
			3.  &emsp;&emsp;`def` `this`(param-list){***optional,default is from base struct***
			4.  &emsp;&emsp;&emsp;`statements`
			5.  &emsp;&emsp;}
			6.  &emsp;&emsp;`def` [`virtual`/`override`] ret-type func-name(param-list){  
			7.  &emsp;&emsp;&emsp;`statements`
	        8.  &emsp;&emsp;} 
			9.  }
		* Notation:
			1. can't capture variables from outer environments,use initial function instead
			2. initialization function will inherit from base class directly by default
	* Lambda expression
	    * Syntax:
	        1. `new` struct-type[^param-name-list]->{`statements`}
	        2. `new` struct-type[^param-name-list]->`expression`
	    * Notation:param-name-list can be:
	        1. (`param-names`)
	        2. param-name
	        
	      This struct should and must have one default function(`virtual`).
	      And lambda expression can ignore params legally
	      
	* Variable declaration
		- Basic-type var-name\[,...] (var-name must begin with one of [a-zA-Z_])
		- Basic-type var-name = initial-value\[,...](with initial-value)
		- Basic-type []... var-name\[,...](for array declaration)
	* Constant:
		- `int`:only decimal integer is supported(ends with '**i**' or less than IntMax)
		- `char`:'character'(character includes escape character `\n`,`\t`,`\r`,`\'`,`\"`,`\?`,`\b`,`\f`,`\\`)
		- `string`:"characters"
		- `real`:just support decimal real number(ends with '**r**' or less than RealMax)
		- `bool`:`true`,`false`
		- `bigint`:integer larger than IntMax(or ends with '**I**')
		- `bigreal`:real larger than RealMax(or ends with '**R**') 
		- `null`:used for array and struct
	* Dynamic array allocation:
		- `new` type [expression];***expression should be integer***
		- `new` struct-type;***struct has no defined initial functions***
		- `new` struct-type([argument,...]);***struct has a defined initial function***
	* Array size getter:(return an integer)
		- `sizeof` array-type-expression
	* Instance check:(return `true` or `false`)
		- expression `instanceof` basic-type
	* Built-in variable:
		- `_line_`:  line number of the source file
		- `_file_`:  file name of the source file
		- `_version_`: compiler version(major-version * 100 + minor-version)
		- `_args_`: command line arguments(runtime variable)
* Extension from native environment
	* java function binding
		* Inherit **extension.Function**
		* override methods:**Value run(List<Value> arguments)* `arguments` is from runtime invocations
		* override methods if need some initialization work before importing
		* importing
			1.  `native`<extension.Function>{
			2.  ***the class-name is not necessary,use *func-name* by default***
			2.  &emsp;&emsp;["class-name"]:ret-type func-name(param-list);
			3. }
	* java class binding
		* Inherit **extension.Function**,
		* override **Struct setup(Token sname, Dictionary dic, TypeTable typeTable)** 
		* and then there comes 2 ways to do ex-struct definition:
		
		    Compose a struct by coding(it feels very bad)
		    
		    Or compose a struct by class **ExtensionStructHelper** as following:
            * use annotation `Init` for struct initialization function
                * `param` for parameters' types
            * use annotation `StructMethod` for struct member function
                * `value` :function name,java method name by default
                * `param` :parameters' types
                * `ret` :return type
                * `virtual` :if the function is virtual
                * `purevirtual` :if the function is pure virtual,if it is set,virtual is set.
            * use annotation `PassThisReference` if you need pass this reference to this natively,
            note that the java method needs an extra arg(typed `Value` or `StructValue`) for `this` in 1st position
            * to refer an extension class type,use class reference
                * $ stands for this extension class
                * \#.`class-name` stands for relative path native extension class
                * \#`class-name` stands for absolute path native extension class
                * if you want to return a class extension struct,
                just use `new StructValue(symbols.Struct.StructPlaceHolder,xxxxx);`,vm will dynamic cast its type
* Features in future
	* Inline function definition:
		- `def` return-type func-name([param-type param-name,...]) = expression;
