package net.backupcup.hexed.entity.blazingSkull

import net.backupcup.hexed.register.RegisterSounds
import net.minecraft.block.BlockState
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.attribute.DefaultAttributeContainer
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.mob.FlyingEntity
import net.minecraft.entity.mob.Monster
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.particle.ParticleTypes
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.util.hit.EntityHitResult
import net.minecraft.util.hit.HitResult
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3d
import net.minecraft.world.RaycastContext
import net.minecraft.world.World
import kotlin.random.Random

class BlazingSkullEntity(
    entityType: EntityType<out FlyingEntity>?,
    world: World?,
    spawnPos: Vec3d = Vec3d(0.0, 0.0, 0.0),
    initialVelocity: Vec3d = Vec3d(0.0, 0.0, 0.0),
    initialTarget: LivingEntity? = null
) : FlyingEntity(entityType, world), Monster {
    companion object {
        fun createAttributes(): DefaultAttributeContainer.Builder {
            return createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 4.0)
                .add(EntityAttributes.GENERIC_ARMOR, 2.0)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 4.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 1.5)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 64.0)
        }
    }

    private var ticksUntilNewTarget: Int
    private var ticksAlive: Int = 0

    init {
        this.experiencePoints = 0

        this.ticksUntilNewTarget = 15
        this.setPos(spawnPos.x, spawnPos.y, spawnPos.z)
        this.velocity = initialVelocity
        this.target = initialTarget
    }

    override fun tick() {
        super.tick()
        this.spawnParticles()

        if((this.world.time % Random.nextInt(40, 80)).toInt() == 0)
            this.world.playSound(
                null, this.blockPos,
                SoundEvents.ENTITY_BLAZE_AMBIENT, SoundCategory.HOSTILE,
                Random.nextDouble(0.25, 0.5).toFloat(), Random.nextDouble(1.0, 1.5).toFloat())

        if (!this.world.isClient) {
            if (this.ticksAlive >= 1200) this.remove(RemovalReason.DISCARDED)

            if (this.ticksUntilNewTarget > 0) {
                this.ticksUntilNewTarget--
                return
            }

            this.ticksAlive++

            if (this.target == null) {
                if (Random.nextInt(100) < 5) { this.randomizeDirection() }
                else { this.findTarget() }
            } else {
                if (target?.isPartOfGame == false || if (target is PlayerEntity) (target as? PlayerEntity)?.isCreative == true else false) {
                    target = null
                    return
                }

                if (this.boundingBox.expand(0.125).intersects(target?.boundingBox) && !this.isDead) {
                    this.explodeYourself()
                    this.remove(RemovalReason.KILLED)
                }

                val distanceSq = this.squaredDistanceTo(target)
                if (distanceSq < this.attributes.getValue(EntityAttributes.GENERIC_FOLLOW_RANGE) * this.attributes.getValue(EntityAttributes.GENERIC_FOLLOW_RANGE)) {
                    this.moveTowardsTarget(target)
                } else {
                    this.target = null
                }
            }

            this.yaw = (MathHelper.atan2(this.velocity.z, this.velocity.x) * (180.0 / Math.PI) - 90.0).toFloat()
            // Adjust pitch based on vertical velocity //doesn't work for some godforsaken reason
            val pitchLimit = 30.0
            this.roll = MathHelper.clamp(-this.velocity.y * pitchLimit, -pitchLimit, pitchLimit).toInt()
        }
    }

    override fun onBlockCollision(state: BlockState) {
        super.onBlockCollision(state)
        if (!state.isSolid) return

        this.explodeYourself()
        this.remove(RemovalReason.KILLED)
    }

    override fun onDeath(damageSource: DamageSource?) {
        this.explodeYourself()
        super.onDeath(damageSource)
    }

    private fun explodeYourself() {
        this.world.createExplosion(this, this.x, this.y, this.z, this.attributes.getValue(EntityAttributes.GENERIC_ATTACK_DAMAGE).toFloat(), World.ExplosionSourceType.NONE)
        this.world.playSound(
            null, this.blockPos,
            RegisterSounds.ACCURSED_ALTAR_HEX, SoundCategory.HOSTILE,
            Random.nextDouble(0.5, 1.0).toFloat(), Random.nextDouble(0.75, 1.25).toFloat())
    }

    private fun findTarget() {
        val nearbyPlayers = world.getEntitiesByClass(
            LivingEntity::class.java,
            this.boundingBox.expand(this.attributes.getValue(EntityAttributes.GENERIC_FOLLOW_RANGE))
        ) { it.isPartOfGame && (if (it is PlayerEntity) !it.isCreative else true) && it !is BlazingSkullEntity }

        if (nearbyPlayers.isNotEmpty()) {
            this.target = nearbyPlayers.minByOrNull { this.squaredDistanceTo(it) }
        }
    }

    private fun randomizeDirection() {
        val newPos = Vec3d(
            this.x + Random.nextDouble(-10.0, 10.0),
            this.y + Random.nextDouble(-5.0, 5.0),
            this.z + Random.nextDouble(-10.0, 10.0)
        )
        this.ticksUntilNewTarget = Random.nextInt(20, 60)

        this.velocity = Vec3d(newPos.x - this.x, newPos.y - this.y, newPos.z - this.z)
            .normalize().multiply(this.attributes.getValue(EntityAttributes.GENERIC_MOVEMENT_SPEED))
    }

    private fun moveTowardsTarget(targetPos: LivingEntity?) {
        if(targetPos == null) return
        val newPos = Vec3d(targetPos.x, targetPos.y + targetPos.height/2, targetPos.z)
        this.velocity = newPos.subtract(this.pos).normalize().multiply(this.attributes.getValue(EntityAttributes.GENERIC_MOVEMENT_SPEED))
    }

    private fun spawnParticles() {
        if (this.world.time % 5 == 0L && Random.nextBoolean()) {
            this.world.addImportantParticle(
                ParticleTypes.LAVA,
                this.x, this.y, this.z,
                0.0, 0.0, 0.0
            )
        } else {
            val particlePos = Vec3d(
                this.x + Random.nextDouble(-0.5, 0.5),
                this.y + Random.nextDouble(0.0, 1.0),
                this.z + Random.nextDouble(-0.5, 0.5))

            if(Random.nextBoolean()) {
                this.world.addImportantParticle(
                    ParticleTypes.SMOKE,
                    particlePos.x, particlePos.y, particlePos.z,
                    0.0, 0.0, 0.0
                )
            } else {
                this.world.addImportantParticle(
                    if (Random.nextBoolean()) ParticleTypes.FLAME else ParticleTypes.SMALL_FLAME,
                    particlePos.x, particlePos.y, particlePos.z,
                    0.0, 0.0, 0.0
                )
            }
        }
    }
}