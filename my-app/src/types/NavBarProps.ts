import { ComponentStates } from '@/types/ComponentStates'

export interface NavBarProps {
  onToggleComponent: (component: keyof ComponentStates) => void
  onHome: () => void
  componentStates: ComponentStates
}
