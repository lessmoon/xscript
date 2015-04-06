package gen;

public class CodeTag {
    /*
     * The Virtual Machine should have registers:
     * IR[0,1],RR[0,1],CR[0,1],SR[0,1]
     * BR[0,1],TR[0,1],AR[0,1]
     * 
     */
    public static final int
            OPRAND_POSITION = 31-4,/*2^5 OPRAND AT MOST*/
            TYPE_0_POSITION = 0,
            TYPE_1_POSITION = 4,
            TYPE_0_MASK_CODE= 0xf,
            TYPE_1_MASK_CODE= TYPE_0_MASK_CODE << TYPE_1_POSITION,
            OPRAND_MASK_CODE= 0x1f << OPRAND_POSITION,
            OTHER_POSITION  = 8;/*2^(26-8)*/
    public static final int /*OPRAND TYPE at highest 3 bits*/
            NIL_CODE  = 0,
            INT_CODE  = 1,REAL_CODE   = 2,BOOL_CODE   = 3,
            CHAR_CODE = 4,STRUCT_CODE = 5,ARRAY_CODE  = 6,
            STR_CODE  = 7,ADDR_CODE   = 8;
    public static final int 
            GET_OP  = 1 << OPRAND_POSITION ,SET_OP   = 2 << OPRAND_POSITION,
            DEF_OP  = 3 << OPRAND_POSITION ,
            PUSH_OP = 5 << OPRAND_POSITION ,POP_OP   = 6 << OPRAND_POSITION,

            /*Control flow */
            RET_OP  = 4 << OPRAND_POSITION ,
            CALL_OP = 7 << OPRAND_POSITION ,JUMP_OP  = 8 << OPRAND_POSITION,
            
            /*Arith expression*/
            CMP_OP  = 9  << OPRAND_POSITION,ADD_OP   = 10 << OPRAND_POSITION,
            MULT_OP = 11 << OPRAND_POSITION,SUB_OP   = 12 << OPRAND_POSITION,
            DIV_OP  = 13 << OPRAND_POSITION,MOD_OP   = 14 << OPRAND_POSITION,
            AND_OP  = 15 << OPRAND_POSITION,OR_OP    = 16 << OPRAND_POSITION,
            /*Unary expression*/
            CONV_OP = 17 << OPRAND_POSITION,
            NEW_OP  = 18 << OPRAND_POSITION,
            SZOF_OP = 19 << OPRAND_POSITION,
            NEG_OP  = 20 << OPRAND_POSITION,
            NOT_OP  = 21 << OPRAND_POSITION,
            UNDEF_OP = 22 << OPRANDE_POSITION,
            /*Member expression*/
            /*
             * | IDX_OP |
             * Code format : ASS_XXX_OP | ADDR_CODE << TYPE_1_POSITION |  INT_CODE
             */
            IDX_OP  = 29 << OPRAND_POSITION,
            /*Assignment expression*/
            /*
             * |ASS_XXX_OP|
             * XR[0] += XR[1]
             * ADDR := XR[0]
             * Code format : ASS_XXX_OP | [AB_]ADDR_CODE << TYPE_1_POSITION |  YYY_CODE
             */
            ASS_ADD_OP  = 23 << OPRAND_POSITION ,
            ASS_SUB_OP  = 24 << OPRAND_POSITION ,
            ASS_MULT_OP = 25 << OPRAND_POSITION,
            ASS_DIV_OP  = 26 << OPRAND_POSITION,
            ASS_MOD_OP  = 27 << OPRAND_POSITION,//Just for int
            
            /*
             * | MOV |
             * Code format : MOV_OP| TAR_REG << (OTHER_POSITION + 4) |SRC_REG <<OTHER_POSITION| YYY_CODE <<  TYPE_1_POSITION |  YYY_CODE
             */
            MOV_OP = 28  << OPRAND_POSITION,
            MOV_1_TO_0 = MOV_OP | 0 << (OTHER_POSITION + 4) | 1 << OTHER_POSITION ,
            MOV_0_TO_1 = MOV_OP | 1 << (OTHER_POSITION + 4) | 0 << OTHER_POSITION ,
            SET_TRUE_IF  = 30 << OPRAND_POSITION,
            ;
    public static final int//Options
            MEM_VAL      = 0,IMMEDIATE_VAL = 1,
            WITH_INITIAL = 1,WITHOUT_INITIAL = 0,
            LOCAL_CALL   = 0,EXTENDED_CALL =  1,
            C_ANY = 0,C_LS = 1,C_GT   =  2,
            C_LE  = 3,C_GE = 4,
            C_NE  = 5,C_EQ = 6,
            C_NZ  = 7,C_ZE = 8,C_NEVER = 9 ;

    public static final int  
           /*Constant load*/
           /*
            * |GET_XCONST|
            * |    ID    | Stored VAL or VAL ID(if string) of this file CONSTANT area
            */
            GET_SCONST = GET_OP  | IMMEDIATE_VAL<<OTHER_POSITION | STR_CODE,
            GET_ICONST = GET_OP  | IMMEDIATE_VAL<<OTHER_POSITION | INT_CODE,
            GET_BCONST = GET_OP  | IMMEDIATE_VAL<<OTHER_POSITION | BOOL_CODE,
            GET_RCONST = GET_OP  | IMMEDIATE_VAL<<OTHER_POSITION | REAL_CODE,
            GET_CCONST = GET_OP  | IMMEDIATE_VAL<<OTHER_POSITION | CHAR_CODE,
           /*Stack operation*/
           /*
            * | DEF_xVAR  |
            */
            DEF_IVAR   = DEF_OP  | INT_CODE,
            DEF_RVAR   = DEF_OP  | REAL_CODE,
            DEF_BVAR   = DEF_OP  | BOOL_CODE,
            DEF_CVAR   = DEF_OP  | CHAR_CODE,
            DEF_SVAR   = DEF_OP  | STR_CODE,
            DEF_TVAR   = DEF_OP  | STRUCT_CODE,
            DEF_AVAR   = DEF_OP  | ARRAY_CODE,
           /*
            * values (will be)stored in registers
            * | [UN]DEF_xVAR_V |
            */
            DEF_VAR_V  = DEF_OP | WITH_INITIAL,
            DEF_IVAR_V = DEF_OP | WITH_INITIAL<<OTHER_POSITION | INT_CODE,
            DEF_RVAR_V = DEF_OP | WITH_INITIAL<<OTHER_POSITION | REAL_CODE,
            DEF_BVAR_V = DEF_OP | WITH_INITIAL<<OTHER_POSITION | BOOL_CODE,
            DEF_CVAR_V = DEF_OP | WITH_INITIAL<<OTHER_POSITION | CHAR_CODE,
            DEF_SVAR_V = DEF_OP | WITH_INITIAL<<OTHER_POSITION | STR_CODE,
            DEF_TVAR_V = DEF_OP | WITH_INITIAL<<OTHER_POSITION | STRUCT_CODE,
            DEF_AVAR_V = DEF_OP | WITH_INITIAL<<OTHER_POSITION | ARRAY_CODE,
            UNDEF_VAR_V  = UNDEF_OP ,
            UNDEF_IVAR_V = UNDEF_OP | INT_CODE,
            UNDEF_RVAR_V = UNDEF_OP | REAL_CODE,
            UNDEF_BVAR_V = UNDEF_OP | BOOL_CODE,
            UNDEF_CVAR_V = UNDEF_OP | CHAR_CODE,
            UNDEF_SVAR_V = UNDEF_OP | STR_CODE,
            UNDEF_TVAR_V = UNDEF_OP | STRUCT_CODE,
            UNDEF_AVAR_V = UNDEF_OP | ARRAY_CODE,
           /*
            * |OPRAND_CODE_|
            */
            PUSH_STACK  = PUSH_OP  | NIL_CODE,POP_STACK = POP_OP  | NIL_CODE,
           /*
            * |OPRAND_CODE_|
            * |STACK_LEVELS|
            */
            PUSH_N_STACK = PUSH_OP  | INT_CODE,POP_N_STACK = POP_OP  | INT_CODE,
            /*Memory operation*/
           /*
            * |OPRAND_CODE_ |
            * |STACK_ADDRESS| Maybe another operator number is assumed to be stored in register
            * |STACK_OFFSET |
            * Code format : xET_OP | [AB_]ADDR_CODE << TYPE_1_POSITION | YYY_CODE
            * Supported Type should have all of the types
            */
           /*
            * |CALL_FUNC     |
            * |FUNC_ADDRESS  | Arguments should be pushed to stack before calling functions
            *  Code format : CALL_OP | LOCAL_CALL<<OTHER_POSITION
            */
            CALL_L = CALL_OP | LOCAL_CALL << OTHER_POSITION,
           /*
            * |CALL_EXTENSION|
            * |FUNCTION_ID   | Extension functions should have an function id(id of stored position)
            * Code format : CALL_OP | EXTENDED_CALL<<OTHER_POSITION
            */
           CALL_E = CALL_OP | EXTENDED_CALL << OTHER_POSITION;
           /*
            *  |RET_FUNCTION| Return value will be stored in xRN(if return value exists)
            * 
            */
           /*
            * |Comparator| Compare between two value(stored in register)
            *              After comparing it will set Carry Flags,Sign Flag
            * Code format : CMP_OP | XXX_CODE
            */
            //CMPC,CMPS,CMPI,CMPR,
            //CMPB,CMPB is for the boolean type,CMPA is for the struct type
            //CMPA&CMPB,it will set all flags on or off(depends on equality)
           /*
            * |JUMP OPRAND|
            * |   OFFSET  |  Always an integer
            * Code format : JMP_OP | JC_XXX << OTHER_POSITION 
            */
           /*
            * Math Calculate
            */
           /*
            * |Math OPRAND|//operating number should be stored in XR[0,1],res in XR[0]
            * Code format : XXX_OP | YYY_CODE
            * INT and CHAR are capable for all operators while
            * REAL can't take mod and STRING just meets add(cat)
            */
           /*
            * |LOGIC OPRAND|//operating number should be stored in BR[0,1],res in BR[0]
            * AND , OR
            * Code format : XXX_OP | BOOL_CODE << TYPE_1_POSITION  | BOOL_CODE
            */
           /*
            * |UNARY OPRAND|
            * Code format : NEG_OP | XXX_CODE
            * Just capable for INT,REAL,CHAR
            */
           /*
            * |CONV OPRAND| //XCONV_TOY,XR[0]=>YR[0]
            * Code format : CONV_OP | XXX_CODE << TYPE_1_POSITION | YYY_CODE
            */
           /*
            * Memory manager
            */
           /*
            * |NEW_CALL|//new size stored in ax,and result return to XAR
            */
            
}