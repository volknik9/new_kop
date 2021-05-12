/*
 *
 * This file is part of Kopycat emulator software.
 *
 * Copyright (C) 2020 INFORION, LLC
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * Non-free licenses may also be purchased from INFORION, LLC, 
 * for users who do not want their programs protected by the GPL. 
 * Contact us for details kopycat@inforion.ru
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 *
 */
package ru.inforion.lab403.kopycat.cores.v850es.instructions.cpu.logic

import ru.inforion.lab403.common.extensions.get
import ru.inforion.lab403.common.extensions.insert
import ru.inforion.lab403.kopycat.cores.base.enums.Datatype
import ru.inforion.lab403.kopycat.cores.base.operands.AOperand
import ru.inforion.lab403.kopycat.cores.v850es.hardware.flags.FlagProcessor
import ru.inforion.lab403.kopycat.cores.v850es.instructions.AV850ESInstruction
import ru.inforion.lab403.kopycat.cores.v850es.operands.v850esVariable
import ru.inforion.lab403.kopycat.modules.cores.v850ESCore



class Bsw(core: v850ESCore, size: Int, vararg operands: AOperand<v850ESCore>):
        AV850ESInstruction(core, Type.VOID, size, *operands) {
    override val mnem = "bsw"

    override val cyChg = true
    override val ovChg = true
    override val sChg = true
    override val zChg = true

    private val result = v850esVariable(Datatype.DWORD)

    // Format XII - imm, reg2, reg3
    override fun execute() {
        val a1 = op2.value(core)
        val res = a1[31..24]
                .insert(a1[23..16], 15..8)
                .insert(a1[15..8], 23..16)
                .insert(a1[7..0], 31..24)
        FlagProcessor.processSwapFlag(core, result) {
            it.byte(core, 0) == 0L ||
                    it.byte(core, 1) == 0L ||
                    it.byte(core, 2) == 0L ||
                    it.byte(core, 3) == 0L
        }
        op3.value(core, res)
    }
}