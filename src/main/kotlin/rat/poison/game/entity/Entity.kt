package rat.poison.game.entity

import rat.poison.game.CSGO.csgoEXE
import rat.poison.game.me
import rat.poison.game.netvars.NetVarOffsets.bSpottedByMask
import rat.poison.game.netvars.NetVarOffsets.dwModel
import rat.poison.game.netvars.NetVarOffsets.iTeamNum
import rat.poison.game.netvars.NetVarOffsets.nSurvivalTeam
import rat.poison.game.netvars.NetVarOffsets.vecOrigin
import rat.poison.game.netvars.NetVarOffsets.vecViewOffset
import rat.poison.game.offsets.ClientOffsets.bDormant
import rat.poison.game.offsets.ClientOffsets.dwIndex
import rat.poison.game.offsets.ClientOffsets.pStudioHdr
import rat.poison.utils.Angle
import rat.poison.utils.extensions.uint
import rat.poison.utils.readCached
import it.unimi.dsi.fastutil.longs.Long2ObjectMap
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap

typealias Entity = Long

internal fun Entity.spotted(): Boolean {
	val meID = csgoEXE.int(me + dwIndex) - 1
	val spottedByMask = csgoEXE.uint(this + bSpottedByMask)
	val result = spottedByMask and (1 shl meID).toLong()
	return result != 0L
}

internal fun Entity.dormant(): Boolean = try {
	csgoEXE.boolean(this + bDormant)
} catch (t: Throwable) {
	false
}

internal fun Entity.team() = csgoEXE.uint(this + iTeamNum)

internal fun Entity.survivalTeam() = csgoEXE.uint(this + nSurvivalTeam)

internal fun Entity.model(): Long = csgoEXE.uint(this + dwModel)

internal fun Entity.studioHdr(): Long = csgoEXE.uint(this + pStudioHdr)

private val entity2Angle: Long2ObjectMap<Angle> = Long2ObjectOpenHashMap(255)

internal fun Entity.position(): Angle = readCached(entity2Angle) {
	x = csgoEXE.float(it + vecOrigin).toDouble()
	y = csgoEXE.float(it + vecOrigin + 4).toDouble()
	z = csgoEXE.float(it + vecOrigin + 8).toDouble() + csgoEXE.float(it + vecViewOffset + 8)
}

internal fun Entity.absPosition(): Angle = readCached(entity2Angle) {
	x = csgoEXE.float(it + vecOrigin).toDouble()
	y = csgoEXE.float(it + vecOrigin + 4).toDouble()
	z = csgoEXE.float(it + vecOrigin + 8).toDouble()
}